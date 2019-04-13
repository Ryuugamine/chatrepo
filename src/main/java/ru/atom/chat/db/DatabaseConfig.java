package ru.atom.chat.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.atom.chat.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


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
        try {
            jdbcTemplate.update("INSERT INTO users(nickname, password, online) VALUES (?,?,?)",
                    new Object[] {user.getNickname(), user.getPassword(), "0"});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User login(String nick, String password){
        return jdbcTemplate.queryForObject(
                "select * from users where nickname = ? AND password = ?", new Object[] { nick, password },
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new User(rs.getInt("id"),  rs.getString("nickname"),
                                rs.getString("password"), rs.getBoolean("online"));
                    }
                });
    }
}