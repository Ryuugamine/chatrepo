package ru.atom.chat.client;

import okhttp3.Response;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.chat.client.ChatClient;
import ru.atom.chat.server.ChatApplication;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatClientTest {
    private static String MY_NAME_IN_CHAT = "I_AM_STUPID";
    private static String MY_MESSAGE_TO_CHAT = "SOMEONE_KILL_ME";
    private static String MY_FALSE_NAME_IN_CHAT = "IAMSTUPID";

    @Test
    public void login() throws IOException {
        Response response = ChatClient.login(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        System.out.println();
        Assert.assertTrue(response.code() == 200 || body.equals("Already logged in:("));
    }

    @Test
    public void viewChat() throws IOException {
        Response response = ChatClient.viewChat();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }


    @Test
    public void viewOnline() throws IOException {
        Response response = ChatClient.viewOnline();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void say() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void logout() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.logout(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        Assert.assertTrue(response.code() == 200);
    }

    @Test
    public void logout1() throws IOException {
        ChatClient.logout(MY_FALSE_NAME_IN_CHAT);
        Response response = ChatClient.logout(MY_FALSE_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        Assert.assertTrue(response.code() == 400);
    }

    @Test
    public void messagesCount() throws IOException {
        Response response = ChatClient.viewMessagesCount();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void rename() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.rename(MY_NAME_IN_CHAT, MY_FALSE_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        Assert.assertTrue(response.code() == 200);
    }

    @Test
    public void clear() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.clear(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        Assert.assertTrue(response.code() == 200);
    }
}