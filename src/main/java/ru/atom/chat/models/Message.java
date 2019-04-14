package ru.atom.chat.models;

public class Message {

    private int id;
    private String from;
    private String message;
    private long time;

    public Message(int id, String from, String message, long time) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.time = time;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
