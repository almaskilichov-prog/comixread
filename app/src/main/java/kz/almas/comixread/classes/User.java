package kz.almas.comixread.classes;

import java.util.ArrayList;

public class User {
    private String id, avatarBase64, username, password;
    public ArrayList<String> readingComicBook;

    public User() {
    }

    public User(String id, String avatarBase64, String username, String password, ArrayList<String> readingComicBook) {
        this.id = id;
        this.avatarBase64 = avatarBase64;
        this.username = username;
        this.password = password;
        this.readingComicBook = readingComicBook;
    }

    public String getId() {
        return id;
    }
    public String getAvatarBase64() {
        return avatarBase64;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public ArrayList<String> getReadingComicBook() {
        return readingComicBook;
    }


    public void setId(String id) {
        this.id = id;
    }
    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setReadingComicBook(ArrayList<String> readingComicBook) {
        this.readingComicBook = readingComicBook;
    }
}
