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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private List<String> messages = new ArrayList<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();
    private DatabaseConfig config;

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in:(");
        }
        usersOnline.put(name, name);
        messages.add("[" + name + "] logged in");
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            path = "test_db_creation",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testDb() {
        if (config == null) {
            config = new DatabaseConfig(jdbcTemplate);
        }

        String msg = "";
        try {
            config.addNewUser(new User(0, "Ryuu", "amahasla", true));
            config.getOnlineList();
            msg += "success";

        } catch (Exception e) {
            msg = e.getMessage();
        }

        return ResponseEntity.ok(msg);
    }


    /**
     * curl -i localhost:8080/chat/online_list
     */
    @RequestMapping(
            path = "online_list",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity onlineList() {
        String responseBody = String.join("\n", usersOnline.keySet());
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&text=Hello everyone in this chat"
     */

    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> say(@RequestParam("name") String name, @RequestParam("text") String text) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("User [" + name + "] not logged in");
        }
        messages.add("[" + name + "]: " + text);

        return ResponseEntity.ok().build();
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */

    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> logout(@RequestParam("name") String name) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("User [" + name + "] not logged in");
        }
        usersOnline.remove(name, name);
        messages.add("[" + name + "] logged out");
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity chat() {
        String responseBody = String.join("\n", messages);
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -i localhost:8080/chat/messages_count
     */
    @RequestMapping(
            path = "messages_count",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity messagesCount() {
        String responseBody = String.valueOf(messages.size());
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/rename -d "name=I_AM_STUPID&newname=IAMSTUPID"
     */

    @RequestMapping(
            path = "rename",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> rename(@RequestParam("name") String name, @RequestParam("newname") String newName) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("User [" + name + "] not logged in");
        }
        usersOnline.remove(name, name);
        usersOnline.put(newName, newName);

        messages.add("[" + name + "] rename to [" + newName + "]");
        return ResponseEntity.ok().build();
    }


    /**
     * curl -X POST -i localhost:8080/chat/clear -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "clear",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> clear(@RequestParam("name") String name) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("User [" + name + "] not logged in");
        }
        messages.clear();
        return ResponseEntity.ok().build();
    }
}