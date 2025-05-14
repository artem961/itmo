package lab6.collection.database;


import common.ConfigLoader;

import java.sql.*;

public class DBManager {
    private final String user;
    private final String password;
    private final String url;

    public DBManager(){
        ConfigLoader configLoader = new ConfigLoader();

        user = configLoader.get("db_user");
        password = configLoader.get("db_password");
        url = configLoader.get("db_url");
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DBException("Не удалось подключиться к базе!\n" + e);
        }

    }
}
