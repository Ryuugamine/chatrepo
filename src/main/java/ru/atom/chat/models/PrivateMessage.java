package ru.atom.chat.models;

public class PrivateMessage {
    private int id;
    private String from;
    private String to;
    private String message;
    private long time;

    public PrivateMessage(int id, String from, String to, String message, long time) {
        this.id = id;
        this.from = from;
        this.to = to;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
