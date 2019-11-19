import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utilizador {
    private String name;
    private Biblioteca userMedia;
    private String email;
    private String passwd;
    private boolean isLogged;
    private List<Utilizador> friends;

    Utilizador(String email, String name) {
        this.name = name;
        this.email = email;

        this.isLogged = false;

        //Null passwd to force user to set it on first login
        //Better alternatives are welcome
        this.passwd = null;
        this.userMedia = new Biblioteca();
        this.friends = new ArrayList<>();
    }

    String getEmail() {
        return email;
    }

    boolean checkPasswd(String passwd) {
        return this.passwd.equals(passwd);
    }

    boolean alreadyLoggedIn() {
        return this.isLogged;
    }

    void login() {
        this.isLogged = true;
    }

    void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    void setName(String name) {
        this.name = name;
    }

    //I want some nice upload over the air
    Media uploadMedia(String mediaPath) throws IOException {
        Path old = Paths.get(mediaPath);
        Path newer = Paths.get(".media");
        if(Files.notExists(newer)) {
            Files.createDirectory(newer);
        }
        Path file = Files.copy(old, newer.resolve(old.getFileName()));
        Media newMedia = new Musica(this, file);
        this.userMedia.addMedia(newMedia);
        return newMedia;
    }

    Biblioteca getUserMedia() {
        return userMedia;
    }
}