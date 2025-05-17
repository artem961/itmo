package lab6.collection.database;

import common.collection.exceptions.ValidationException;
import common.collection.models.Coordinates;
import common.collection.models.Flat;
import common.collection.models.Furnish;
import common.collection.models.Transport;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.HashSet;

public class FlatRepository implements Repository<Flat> {
    private final DBManager dbManager;
    private final HouseRepository houseRepository;

    public FlatRepository() {
        dbManager = new DBManager();
        houseRepository = new HouseRepository();
    }

    @Override
    public int insert(Flat flat) {
        try {
            String query = "select insertFlat(?, ?, ?, ?, ?, ?, ?, ?::furnish, ?::transport, ?, ?)";
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stat = connection.prepareStatement(query);

            stat.setString(1, flat.getName());
            stat.setFloat(2, flat.getCoordinates().getX());
            stat.setDouble(3, flat.getCoordinates().getY());
            stat.setDate(4, Date.valueOf(flat.getCreationDate()));
            stat.setFloat(5, flat.getArea());
            stat.setInt(6, flat.getNumberOfRooms());
            stat.setLong(7, flat.getHeight());
            stat.setInt(11, flat.getUserId());

            if (flat.getFurnish() != null) stat.setString(8, flat.getFurnish().name());
            else stat.setNull(8, Types.OTHER);

            if (flat.getTransport() != null) stat.setString(9, flat.getTransport().name());
            else stat.setNull(9, Types.OTHER);

            if (flat.getHouse() != null) {
                int houseId = houseRepository.insert(flat.getHouse());
                stat.setInt(10, houseId);
            } else stat.setNull(10, Types.OTHER);

            ResultSet result = stat.executeQuery();
            if (result.next()){
                flat.setId(result.getInt(1));
                connection.commit();
                return 1;
            } else{
                connection.rollback();
                return 0;
            }
        } catch (SQLException e) {
            throw new DBException("Не удалось вставить flat!\n" + e);
        }
    }

    @Override
    public int updateById(Flat flat, Integer id) {
        try {
            String query = "UPDATE flats SET name=?, x=?, y=?," +
                    " date=?, area=?, numb_of_rooms=?," +
                    " height=?, furnish=?::furnish, transport=?::transport, house=?, user_id=? WHERE id=?";

            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stat = connection.prepareStatement(query);

            stat.setString(1, flat.getName());
            stat.setFloat(2, flat.getCoordinates().getX());
            stat.setDouble(3, flat.getCoordinates().getY());
            stat.setDate(4, Date.valueOf(flat.getCreationDate()));
            stat.setFloat(5, flat.getArea());
            stat.setInt(6, flat.getNumberOfRooms());
            stat.setLong(7, flat.getHeight());

            if (flat.getFurnish() != null) stat.setString(8, flat.getFurnish().name());
            else stat.setNull(8, Types.OTHER);

            if (flat.getTransport() != null) stat.setString(9, flat.getTransport().name());
            else stat.setNull(9, Types.OTHER);

            if (flat.getHouse() != null) {
                int houseId = houseRepository.insert(flat.getHouse());
                stat.setInt(10, houseId);
            } else stat.setNull(10, Types.OTHER);

            stat.setInt(11, flat.getUserId());
            stat.setInt(12, id);
            int result = stat.executeUpdate();
            connection.commit();
            return result;
        } catch (SQLException e) {
            throw new DBException("Не удалось обновить по id!\n" + e);
        }
    }

    @Override
    public int removeById(Integer id) {
        try {
            String query = "DELETE FROM flats WHERE id=?";
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(query);
            stat.setInt(1, id);
            int result = stat.executeUpdate();
            connection.commit();
            return result;
        } catch (SQLException e) {
            throw new DBException("Не удалось удалить по id!\n" + e);
        }
    }

    @Override
    public Flat selectById(Integer id) {
        return null;
    }

    @Override
    public HashSet<Flat> selectAll() {
        try {
            HashSet<Flat> flats = new HashSet<>();
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);
            ResultSet result = connection.createStatement()
                    .executeQuery("SELECT * FROM flats");

            Flat flat;
            while (result.next()) {
                flat = new Flat(
                        result.getString("name"),
                        new Coordinates(result.getFloat("x"), result.getDouble("y")),
                        result.getFloat("area"),
                        result.getInt("numb_of_rooms"),
                        result.getLong("height"),
                        Transport.valueOf(result.getString("transport")),
                        null);

                if (result.getString("furnish") != null) {
                    flat.setFurnish(Furnish.valueOf(result.getString("furnish")));
                }

                flat.setId(result.getInt("id"));
                flat.setCreationDate(result.getDate("date").toLocalDate());
                flat.setHouse(houseRepository.selectById(result.getInt("house")));
                flat.setUserId(result.getInt("user_id"));
                flats.add(flat);
            }
            connection.commit();
            return flats;

        } catch (SQLException e) {
            throw new DBException("Не удалось получить выборку flat!\n" + e);
        } catch (ValidationException e) {
            throw new DBException("В базе хранились не валидные квартиры!\n" + e);
        }
    }

    @Override
    public int removeAll() {
        try {
            String query = "TRUNCATE TABLE flats";
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            int res = connection.createStatement().executeUpdate(query);
            connection.commit();
            return res;
        } catch (SQLException e) {
            throw new DBException("Не удалось очистить коллекцию!\n" + e);
        }
    }

    public int removeAll(Integer userId){
        try {
            String query = "DELETE FROM flats WHERE user_id=?";
            Connection connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(query);
            stat.setInt(1, userId);
            int res = stat.executeUpdate();
            connection.commit();
            return res;
        } catch (SQLException e) {
            throw new DBException("Не удалось удалить объекты пользователя!\n" + e);
        }
    }
}
