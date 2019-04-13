package ru.atom.chat.models;

public class User {

    private int id;
    private String nickname;
    private String password;
    private boolean online;

    public User(int id, String nickname, String password, boolean online) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.online = online;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getId() {
        return id;
    }
}
