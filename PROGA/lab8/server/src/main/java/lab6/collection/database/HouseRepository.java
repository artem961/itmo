package lab6.collection.database;

import common.collection.exceptions.ValidationException;
import common.collection.models.House;
import lab6.collection.database.connection.DBManager;
import lombok.Cleanup;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class HouseRepository implements Repository<House> {
    private final DBManager dbManager;

    public HouseRepository(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public int insert(House house) {
        try {
            String query = "SELECT insertHouse(?, ?, ?)";
            @Cleanup Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(query);

            stat.setString(1, house.getName());
            stat.setInt(2, house.getYear());
            stat.setLong(3, house.getNumberOfFlatsOnFloor());

            ResultSet result = stat.executeQuery();
            if (result.next()) {
                connection.commit();
                return result.getInt(1);
            } else {
                connection.rollback();
                return -1;
            }
        } catch (SQLException e) {
            throw new DBException("Не удалось вставить дом!\n" + e);
        }
    }

    public List<Integer> insert(Collection<House> houses){
        try {
            String query = "INSERT INTO houses (name, year, num_of_flats) VALUES (?, ?, ?)";
            @Cleanup Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stat = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (House house: houses) {
                stat.setString(1, house.getName());
                stat.setInt(2, house.getYear());
                stat.setLong(3, house.getNumberOfFlatsOnFloor());
                stat.addBatch();
            }
            stat.executeBatch();
            ResultSet result = stat.getGeneratedKeys();

            if (!result.wasNull()) {
                connection.commit();
            } else {
                connection.rollback();
                return null;
            }

            List<Integer> ids = new ArrayList<>();
            while (result.next()){
                ids.add(result.getInt(1));
            }
            return ids;
        } catch (SQLException e) {
            throw new DBException("Не удалось вставить дом!\n" + e);
        }
    }

    @Override
    public int updateById(House house, Integer id) {
        return 0;
    }

    @Override
    public int removeById(Integer id) {
        return 0;
    }

    @Override
    public House selectById(Integer id) {
        try {
            String query = "SELECT * FROM houses WHERE id=?";
            @Cleanup Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(query);
            stat.setInt(1, id);
            ResultSet result = stat.executeQuery();

            if (result.next()) {
                connection.commit();
                return new House(result.getString("name"),
                        result.getInt("year"),
                        result.getLong("num_of_flats"));
            }
            connection.rollback();
            return null;
        } catch (SQLException e) {
            throw new DBException("Не удалось выбрать house по id!\n" + e);
        } catch (ValidationException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public HashSet<House> selectAll() {
        return null;
    }

    @Override
    public int removeAll() {
        return 0;
    }
}