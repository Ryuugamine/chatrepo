package ru.atom.chat.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.atom.chat.models.Message;
import ru.atom.chat.models.PrivateMessage;
import ru.atom.chat.models.User;

import java.util.ArrayList;
import java.util.List;


public class DatabaseConfig {

    JdbcTemplate jdbcTemplate;

    public DatabaseConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        initDatabase();
    }

    private void initDatabase() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users(" +
                "id SERIAL, nickname VARCHAR(255), password VARCHAR(255), online BIT)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS messages(" +
                "id SERIAL, sender VARCHAR(255), message TEXT, time BIGINT)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS private_chats(" +
                "id SERIAL, sender VARCHAR(255), to_user VARCHAR(255), message TEXT, time BIGINT)");
    }

    public boolean addNewUser(User user) {
        SqlRowSet usersSet = jdbcTemplate.queryForRowSet(
                "select * from users where nickname = ?", new Object[]{user.getNickname()});

        if (usersSet.first()) {
            return false;
        } else {
            jdbcTemplate.update("INSERT INTO users(nickname, password, online) VALUES (?,?,?)",
                    new Object[]{user.getNickname(), user.getPassword(), "false"});
            return true;
        }
    }

    public User login(String nick, String password) {
        SqlRowSet usersSet = jdbcTemplate.queryForRowSet(
                "select * from users where nickname = ? AND password = ?", new Object[]{nick, password});

        if (usersSet.first()) {
            jdbcTemplate.update(
                    "update users set online = ? where id = ?",
                    new Object[]{"true", usersSet.getString("id")});
            return new User(usersSet.getInt("id"), usersSet.getString("nickname"),
                    usersSet.getString("password"), usersSet.getBoolean("online"));
        } else {
            return null;
        }
    }

    public List<User> getOnlineList() {
        SqlRowSet usersSet = jdbcTemplate.queryForRowSet(
                "select * from users where online = ?", new Object[]{"true"});

        if (usersSet.first()) {
            List<User> users = new ArrayList<>();
            do {
                users.add(new User(usersSet.getInt("id"), usersSet.getString("nickname"),
                        usersSet.getString("password"), usersSet.getBoolean("online")));
            } while (usersSet.next());
            return users;
        }

        return null;
    }

    public boolean logout(int id) {
        jdbcTemplate.update(
                "update users set online = ? where id = ?",
                new Object[]{"false", String.valueOf(id)});
        return true;
    }

    public List<Message> getChatHistory() {
        SqlRowSet messageSet = jdbcTemplate.queryForRowSet(
                "select * from messages");
        if (messageSet.first()) {
            List<Message> messages = new ArrayList<>();
            do {
                messages.add(new Message(messageSet.getInt("id"), messageSet.getString("sender"),
                        messageSet.getString("message"), messageSet.getLong("time")));
            } while (messageSet.next());
            return messages;
        } else {
            return null;
        }
    }

    public List<Message> getMessagesFromCurrentUser(String userName) {
        SqlRowSet messageSet = jdbcTemplate.queryForRowSet(
                "select * from messages where sender = ?", new Object[] { userName });
        if (messageSet.first()) {
            List<Message> messages = new ArrayList<>();
            do {
                messages.add(new Message(messageSet.getInt("id"), messageSet.getString("sender"),
                        messageSet.getString("message"), messageSet.getLong("time")));
            } while (messageSet.next());
            return messages;
        } else {
            return null;
        }
    }


    public boolean addNewMessage(Message msg) {
        jdbcTemplate.update("INSERT INTO messages(sender, message, time) VALUES (?,?,?)",
                new Object[]{msg.getFrom(), msg.getMessage(), msg.getTime()});
        return true;
    }


    public List<PrivateMessage> getPrivateChatHistory(String user1, String user2) {
        SqlRowSet messageSet = jdbcTemplate.queryForRowSet(
                "select * from messages where (sender = ? AND to = ?) OR (sender = ? AND to = ?)",
                new Object[] { user1, user2, user2, user1 });
        if (messageSet.first()) {
            List<PrivateMessage> messages = new ArrayList<>();
            do {
                messages.add(new PrivateMessage(messageSet.getInt("id"),
                        messageSet.getString("sender"),
                        messageSet.getString("to"),
                        messageSet.getString("message"),
                        messageSet.getLong("time")));
            } while (messageSet.next());
            return messages;
        } else {
            return null;
        }
    }

    public boolean addNewPrivateMessage(PrivateMessage msg) {
        jdbcTemplate.update("INSERT INTO private_chats(sender, to, message, time) VALUES (?,?,?,?)",
                new Object[]{msg.getFrom(), msg.getTo(), msg.getMessage(), msg.getTime()});
        return true;
    }
}