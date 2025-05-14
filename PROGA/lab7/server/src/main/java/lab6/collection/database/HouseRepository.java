package lab6.collection.database;

import common.collection.exceptions.ValidationException;
import common.collection.models.House;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class HouseRepository implements Repository<House> {
    private final DBManager dbManager;

    public HouseRepository(){
        this.dbManager = new DBManager();
    }

    @Override
    public int insert(House house) {
        try {
            String query = "SELECT insertHouse(?, ?, ?)";
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(query);

            stat.setString(1, house.getName());
            stat.setInt(2, house.getYear());
            stat.setLong(3, house.getNumberOfFlatsOnFloor());

            ResultSet result = stat.executeQuery();
            if (result.next()) {
                connection.commit();
                return result.getInt(1);
            }
            connection.rollback();
            return -1;
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
            Connection connection = dbManager.getConnection();
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
