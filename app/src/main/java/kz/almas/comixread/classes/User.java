package kz.almas.comixread.classes;

import java.util.ArrayList;

public class User {
    private String id, avatarBase64, name, username, email;
    public ArrayList<String> readingComicBook;

    public User() {
    }

    public User(String id, String avatarBase64, String name, String username, String email, ArrayList<String> readingComicBook) {
        this.id = id;
        this.avatarBase64 = avatarBase64;
        this.name = name;
        this.username = username;
        this.email = email;
        this.readingComicBook = readingComicBook;
    }

    public String getId() {
        return id;
    }
    public String getAvatarBase64() {
        return avatarBase64;
    }
    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
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
    public void setName(String name) {
        this.name = name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setReadingComicBook(ArrayList<String> readingComicBook) {
        this.readingComicBook = readingComicBook;
    }
}
