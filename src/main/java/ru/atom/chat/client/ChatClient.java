package ru.atom.chat.client;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.io.IOException;


public class ChatClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    //POST host:port/chat/login?name=my_name
    public static Response login(String name) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/login?name=" + name)
                .build();

        return client.newCall(request).execute();
    }

    //GET host:port/chat/chat
    public static Response viewChat() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/chat")
                .addHeader("host", HOST + PORT)
                .build();
        return client.newCall(request).execute();
    }

    //POST host:port/chat/say?name=my_name
    //Body: "msg='my_message'"
    public static Response say(String name, String msg) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/say?name=" + name + "&text=" + msg)
                .build();
        return client.newCall(request).execute();
    }

    //GET host:port/chat/online //
    public static Response viewOnline() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/online_list")
                .addHeader("host", HOST + PORT)
                .build();
        return client.newCall(request).execute();
    }

    //POST host:port/chat/logout?name=my_name
    public static Response logout(String name) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/logout?name=" + name)
                .build();
        return client.newCall(request).execute();
    }

    public static Response viewMessagesCount() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/messages_count")
                .addHeader("host", HOST + PORT)
                .build();
        return client.newCall(request).execute();
    }

    //POST host:port/chat/rename?name=my_name
    //Body: "newName='newname'"
    public static Response rename(String name, String newName) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/rename?name=" + name + "&newname=" + newName)
                .build();
        return client.newCall(request).execute();
    }

    //POST host:port/chat/clear
    public static Response clear(String name) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/clear?name=" + name)
                .build();
        return client.newCall(request).execute();
    }
}