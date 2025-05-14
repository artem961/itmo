package lab6.collection.database;

import common.collection.exceptions.ValidationException;
import common.collection.models.*;

import java.sql.*;
import java.util.HashSet;

public class DBQueryManager {
    private final DBManager dbManager;

    public DBQueryManager() {
        dbManager = new DBManager();
    }

    public HashSet<Flat> selectFlats() {
        HashSet<Flat> flats = new HashSet<>();

        try (Connection connection = dbManager.getConnection()) {
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
                        //Furnish.valueOf(result.getString("furnish")),
                        Transport.valueOf(result.getString("transport")),
                        null);

                flat.setId(result.getInt("id"));
                flat.setCreationDate(result.getDate("date").toLocalDate());
                flat.setHouse(selectHouseById(result.getInt("house")));
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

    public int insertFlat(Flat flat) {
        String query = "INSERT INTO flats (name, x, y, date, area, numb_of_rooms, height, furnish, transport, house) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?::furnish, ?::transport, ?)";
        try (Connection connection = dbManager.getConnection()) {
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
                int houseId = insertHouse(flat.getHouse(), connection);
                stat.setInt(10, houseId);
            } else stat.setNull(10, Types.OTHER);


            int res = stat.executeUpdate();

            flat.setId(getLastFlatId(connection));
            connection.commit();


            return res;
        } catch (SQLException e) {
            throw new DBException("Не удалось вставить квартиру!\n" + e);
        }
    }

    /**
     * @param house Дом
     * @return id Вставленного house
     * @throws SQLException
     */
    private int insertHouse(House house, Connection connection) throws SQLException {
        String query = "SELECT insertHouse(?, ?, ?)";
        PreparedStatement stat = connection.prepareStatement(query);

        stat.setString(1, house.getName());
        stat.setInt(2, house.getYear());
        stat.setLong(3, house.getNumberOfFlatsOnFloor());

        ResultSet result = stat.executeQuery();
        if (result.next()) {
            return result.getInt(1);
        }
        return -1;
    }

    public int updateFlatById(Flat flat, Integer id) {
        String query = "UPDATE flats SET name=?, x=?, y=?," +
                " date=?, area=?, numb_of_rooms=?," +
                " height=?, furnish=?::furnish, transport=?::transport, house=? WHERE id=?";

        try (Connection connection = dbManager.getConnection()) {
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
                int houseId = insertHouse(flat.getHouse(), connection);
                stat.setInt(10, houseId);
            } else stat.setNull(10, Types.OTHER);

            stat.setInt(11, id);
            int result = stat.executeUpdate();
            connection.commit();
            return result;
        } catch (SQLException e) {
            throw new DBException("Не удалось обновить по id!\n" + e);
        }
    }

    public int removeFlatById(Integer id) {
        String query = "DELETE FROM flats WHERE id=?";
        try (Connection connection = dbManager.getConnection()) {
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

    public int removeAllFlats() {
        String query = "TRUNCATE TABLE flats";

        try (Connection connection = dbManager.getConnection()) {
            connection.setAutoCommit(false);
            int res = connection.createStatement().executeUpdate(query);
            connection.commit();
            return res;
        } catch (SQLException e) {
            throw new DBException("Не удалось очистить коллекцию!\n" + e);
        }
    }

    private House selectHouseById(Integer id) throws SQLException, ValidationException {
        String query = "SELECT * FROM houses WHERE id=?";

        try (Connection connection = dbManager.getConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(query);
            stat.setInt(1, id);
            ResultSet result = stat.executeQuery();
            connection.commit();
            if (result.next()) {
                return new House(result.getString("name"),
                        result.getInt("year"),
                        result.getLong("num_of_flats"));
            }
        }
        return null;
    }

    private int getLastFlatId(Connection connection) throws SQLException {
        String query = "SELECT last_value FROM flats_id_seq;";

        ResultSet result = connection.createStatement().executeQuery(query);

        if (result.next()) {
            return result.getInt(1);
        } else {
            throw new SQLException("Не удалось получить flat id!");
        }
    }
}
