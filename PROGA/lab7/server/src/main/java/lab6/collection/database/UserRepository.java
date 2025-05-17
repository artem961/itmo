package lab6.collection.database;

import common.network.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class UserRepository implements Repository<User> {
    private final DBManager dbManager;

    public UserRepository() {
        dbManager = new DBManager();
    }

    @Override
    public int insert(User user) {
        try {
            String query = "SELECT insertUser(?, ?)";
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stat = connection.prepareStatement(query);

            stat.setString(1, user.name());
            stat.setString(2, user.hashedPassword());

            ResultSet result = stat.executeQuery();
            if (result.next()) {
                connection.commit();
                return result.getInt(1);
            } else {
                connection.rollback();
                return -1;
            }
        } catch (SQLException e) {
            throw new DBException("Не удалось вставить user!\n" + e);
        }
    }

    @Override
    public int updateById(User entity, Integer id) {
        return 0;
    }

    @Override
    public int removeById(Integer id) {
        return 0;
    }

    @Override
    public User selectById(Integer id) {
        try {
            String query = "SELECT * FROM users WHERE id=?";
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(query);
            stat.setInt(1, id);
            ResultSet result = stat.executeQuery();

            if (result.next()){
                connection.commit();
                return new User(result.getInt("id"),
                        result.getString("name"),
                        result.getString("password"));
            } else{
                connection.rollback();
                return null;
            }
        } catch (SQLException e){
            throw new DBException("Не удалось получить user по id!\n" + e);
        }
    }

    public User selectByNameAndPass(String name, String password){
        try {
            String query = "SELECT * FROM users WHERE name=? and password=?";
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1, name);
            stat.setString(2, password);
            ResultSet result = stat.executeQuery();

            if (result.next()){
                connection.commit();
                return new User(result.getInt("id"),
                        result.getString("name"),
                        result.getString("password"));
            } else{
                connection.rollback();
                return null;
            }
        } catch (SQLException e){
            throw new DBException("Не удалось получить user по id!\n" + e);
        }
    }

    @Override
    public HashSet<User> selectAll() {
        return null;
    }

    @Override
    public int removeAll() {
        return 0;
    }
}
