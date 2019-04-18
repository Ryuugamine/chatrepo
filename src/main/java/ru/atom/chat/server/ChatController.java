package ru.atom.chat.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import ru.atom.chat.db.DatabaseConfig;
import ru.atom.chat.models.User;
import ru.atom.chat.models.Message;
import ru.atom.chat.models.PrivateMessage;
import ru.atom.chat.models.UserResponse;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.Date;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private List<String> messages = new ArrayList<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();
    private int userIDcout = 0;
    private int msgIDcout = 0;
    private DatabaseConfig config;

    @PostConstruct
    private void init() {
        if (config == null) {
            config = new DatabaseConfig(jdbcTemplate);
        }
    }

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserResponse login(@RequestParam("nick") String nick, @RequestParam("password") String password) {
        UserResponse resp = new UserResponse();
        resp.setUser(config.login(nick, password));
        if (resp.getUser() != null) {

            Date date = new Date();
            long time = date.getTime();

            msgIDcout++;
            Message msg = new Message(msgIDcout, "System", nick + " online", time);
            config.addNewMessage(msg);

            resp.setMessage("success");
            return resp;
        } else {
            resp.setMessage("Wrong login or password");
            return resp;
        }

    }

    @RequestMapping(
            path = "add_new_user",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addNewUser(@RequestParam("nick") String nick,
                                             @RequestParam("password") String password) {
        String msg2 = "";
        try {
            if (config.addNewUser(new User(nick, password, true))) {
                Date date = new Date();
                long time = date.getTime();

                msgIDcout++;
                Message msg = new Message(msgIDcout, "System", nick + " online", time);
                config.addNewMessage(msg);
                msg2 += "success";
            } else {
                msg2 += "User with such nick already exists";
            }
        } catch (Exception e) {
            msg2 = e.getMessage();
        }

        return ResponseEntity.ok().body(msg2);
    }


    /**
     * curl -i localhost:8080/chat/online_list
     */
    @RequestMapping(
            path = "online_list",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<User> onlineList() {
        List<User> resp = new ArrayList<>();

        String msg = "";
        try {
            resp = config.getOnlineList();
            msg += "success";

        } catch (Exception e) {
            msg = e.getMessage();
        }

        return resp;
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&text=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> say(@RequestParam("name") String name,
                                      @RequestParam("text") String text, @RequestParam("time") long time) {
        msgIDcout++;
        Message msg = new Message(msgIDcout, name, text, time);
        config.addNewMessage(msg);
        return ResponseEntity.ok().build();
    }


    @RequestMapping(
            path = "private_say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> privateSay(@RequestParam("nameFrom") String nameFrom,
                                             @RequestParam("nameTo") String nameTo,
                                             @RequestParam("text") String text,
                                             @RequestParam("time") long time) {
        msgIDcout++;
        PrivateMessage msg = new PrivateMessage(msgIDcout, nameFrom, nameTo, text, time);
        config.addNewPrivateMessage(msg);
        return ResponseEntity.ok().build();
    }


    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */

    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> logout(@RequestParam("id") int id) {
        config.logout(id);
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET)
    public List<Message> chat() {
        return config.getChatHistory();
    }


    @RequestMapping(
            path = "private_chat",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<PrivateMessage> privateChat(@RequestParam("name1") String name1, @RequestParam("name2") String name2) {
        return config.getPrivateChatHistory(name1, name2);
    }

    @RequestMapping(
            path = "getMessagesFromCurrentUser",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<Message> getMessagesFromCurrentUser(@RequestParam("name1") String name) {
        return config.getMessagesFromCurrentUser(name);
    }

}