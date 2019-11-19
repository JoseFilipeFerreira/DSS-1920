import Exceptions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MediaCenter {
    //Saving each user library is easier to remove media
    private Map<String, Biblioteca> mainLibrary;
    private Map<String, Utilizador> registedUsers;

    MediaCenter() {
        this.mainLibrary = new HashMap<>();
        this.registedUsers = new HashMap<>();
    }

    //This needs to be better managed
    Utilizador createUser(Utilizador user, String email, String name)
            throws PermissionDeniedException, UserExistsException {
        if(user instanceof Administrador) {
            if(this.registedUsers.containsKey(email))
                throw new UserExistsException();
            Utilizador novo = new Utilizador(email, name);
            this.registedUsers.put(email, novo);
            this.mainLibrary.put(email, novo.getUserMedia());
            return novo;
        }
        throw new PermissionDeniedException();
    }

    void passwd(Utilizador u, String old_passwd, String new_passwd)
            throws NonSettedPasswdException, InvalidPasswordException {
        u.checkPasswd(old_passwd);
        u.setPasswd(new_passwd);
    }

    void chName(Utilizador u, String new_name) {
        u.setName(new_name);
    }

    void rmUser(Utilizador admin, Utilizador to_rm)
            throws PermissionDeniedException {
        if(! (admin instanceof Administrador))
            throw new PermissionDeniedException();
        this.mainLibrary.remove(to_rm.getEmail());
        this.registedUsers.remove(to_rm.getEmail());
    }

    void rmUser(Utilizador admin, String to_rm)
            throws PermissionDeniedException {
        if(! (admin instanceof Administrador))
            throw new PermissionDeniedException();
        this.mainLibrary.remove(to_rm);
        this.registedUsers.remove(to_rm);
    }

    //Allow multiple logins, but I don't think that's a good idea
    Utilizador login(String user, String passwd)
            throws NonExistentUserException, InvalidPasswordException,
            AlreadyLoggedInException, NonSettedPasswdException
    {
        if(!this.registedUsers.containsKey(user)) {
            throw new NonExistentUserException();
        }
        Utilizador log = this.registedUsers.get(user);
        log.checkPasswd(passwd);
        log.login();
        return log;
    }

    void uploadMedia(Utilizador user, String path) {
        try {
            user.uploadMedia(path);
        } catch (IOException ignored) {}
    }

    //TODO We can implement partial searching in the future
    //It would be nice for the gui guys
    //I even think we can use the youtube api to play search videos
    Optional<Media> searchByName(String name) {
        return this.mainLibrary.values()
                .stream()
                .flatMap(x -> x.getLibrary().values().stream())
                .filter(x -> x.getName().equals(name))
                .findFirst();
    }

    public static void main(String[] args) {
        MediaCenter mdia = new MediaCenter();
        Administrador admin = new Administrador("abc", "def");
        try {
            Utilizador user = mdia.createUser(admin, "adeus", "ola");

            //Pls remove this before the due date
            mdia.uploadMedia(user, "/home/hitler/Downloads/ola.mp3");
            Optional<Media> media = mdia.searchByName("ola.mp3");
            media.orElseThrow().play();
        } catch (PermissionDeniedException | UserExistsException ignored) {}
    }
}