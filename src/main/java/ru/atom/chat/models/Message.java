package ru.atom.chat.models;

public class Message {

    private int id;
    private String from;
    private String message;

    public Message(int id, String from, String message) {
        this.id = id;
        this.from = from;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }
}
