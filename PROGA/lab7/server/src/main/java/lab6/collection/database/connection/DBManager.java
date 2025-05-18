package lab6.collection.database.connection;


import common.ConfigLoader;
import lab6.collection.database.DBException;

import java.sql.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class DBManager {
    private final String user;
    private final String password;
    private final String url;
    private final int poolSize;
    private final BlockingQueue<PooledConnection> connectionPool;

    public DBManager(int poolSize){
        ConfigLoader configLoader = new ConfigLoader("database.properties");
        user = configLoader.get("db_user");
        password = configLoader.get("db_password");
        url = configLoader.get("db_url");

        this.poolSize = poolSize;
        this.connectionPool = new ArrayBlockingQueue<>(poolSize);
        initPool();
    }

    private void initPool(){
        for (int i = 0; i < poolSize; i++){
            connectionPool.add(new PooledConnection(createConnection(), connectionPool));
        }
    }

    private Connection createConnection(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DBException("Не удалось подключиться к базе!\n" + e);
        }
    }

    public Connection getConnection() {
        try {
            synchronized (connectionPool) {
                Connection connection = connectionPool.poll(5, TimeUnit.SECONDS);
                if (connection == null){
                    throw new DBException("Нет свободного соединения!");
                } else{
                    return connection;
                }
            }
        } catch (InterruptedException e) {
            throw new DBException("Не удалось получить соединение к базе!\n" + e);
        }
    }
}
