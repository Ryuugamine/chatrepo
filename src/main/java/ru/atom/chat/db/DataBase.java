package ru.atom.chat.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    static final String DB_URL = "postgres://ggegylgkxamtvh:83e8cbc7f2b00d338a3a77bfaea3be9a6d739e56a2f281" +
            "f816b57abbcb124e21@ec2-79-125-2-142.eu-west-1.compute.amazonaws.com:5432/d43mg6nhnlv3bo";
    static final String USER = "ggegylgkxamtvh";
    static final String PASS = "83e8cbc7f2b00d338a3a77bfaea3be9a6d739e56a2f281f816b57abbcb124e21";

    private String msg;

    public DataBase() {
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

            msg = "You successfully connected to database now";

        } catch (SQLException e) {
            msg = "So close";
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
