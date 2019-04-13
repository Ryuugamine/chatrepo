package ru.atom.chat.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
                "select * from users where nickname = ?", new Object[] { user.getNickname() });

        if(usersSet.first()){
            return false;
        } else {
            jdbcTemplate.update("INSERT INTO users(nickname, password, online) VALUES (?,?,?)",
                    new Object[]{user.getNickname(), user.getPassword(), "true"});
            return true;
        }
    }

    public User login(String nick, String password){
        SqlRowSet usersSet = jdbcTemplate.queryForRowSet(
                "select * from users where nickname = ? AND password = ?", new Object[] { nick, password });

        if(usersSet.first()) {
           return new User(usersSet.getInt("id"), usersSet.getString("nickname"),
                    usersSet.getString("password"), usersSet.getBoolean("online"));
        } else {
            return null;
        }
    }

    public List<User> getOnlineList(){
        SqlRowSet usersSet = jdbcTemplate.queryForRowSet(
                "select * from users where online = ?", new Object[] { "true" });

        if(usersSet.first()) {
            List<User> users = new ArrayList<>();
            do {
                users.add(new User(usersSet.getInt("id"), usersSet.getString("nickname"),
                        usersSet.getString("password"), usersSet.getBoolean("online")));
            } while (usersSet.next());
            return users;
        }

        return null;
    }
}