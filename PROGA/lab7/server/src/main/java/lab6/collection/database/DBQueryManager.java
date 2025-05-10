package lab6.collection.database;

import common.collection.exceptions.ValidationException;
import common.collection.models.*;

import java.sql.*;
import java.util.HashSet;
import java.util.List;

public class DBQueryManager {
    private final DBManager dbManager;

    public DBQueryManager(){
        dbManager = new DBManager();
    }

    public HashSet<Flat> selectFlats() throws SQLException, ValidationException {
        HashSet<Flat> flats = new HashSet<>();

        ResultSet result = dbManager.getConnection().createStatement()
                .executeQuery("SELECT * FROM flats");

        Flat flat;
        while (result.next()){
            flat = new Flat(
                    result.getString(2),
                    new Coordinates(result.getFloat(3), result.getDouble(4)),
                    result.getFloat(6),
                    result.getInt(7),
                    result.getLong(8),
                    Furnish.valueOf(result.getString(9)),
                    Transport.valueOf(result.getString(10)),
                    null);
            flat.setId(result.getInt(1));
            flat.setCreationDate(result.getDate(5).toLocalDate());
        }
        return flats;
    }

    public int insertFlat(Flat flat) throws SQLException {
        String query = "INSERT INTO flats (name, x, y, date, area, numb_of_rooms, height, furnish, transport, house) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?::furnish, ?::transport, ?)";
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

        if (flat.getHouse() != null){
            int houseId = insertHouse(flat.getHouse(), connection);
            stat.setInt(10, houseId);
        } else stat.setNull(10, Types.OTHER);


        int res = stat.executeUpdate();
        try {
            flat.setId(getLastFlatId(connection));
            connection.commit();
        } catch (ValidationException e) {
            connection.rollback();
            throw new SQLException(e);
        }

        return res;
    }

    /**
     *
     * @param house
     * @return id Вставленного house
     * @throws SQLException
     */
    public int insertHouse(House house, Connection connection) throws SQLException {
        String query = "SELECT insertHouse(?, ?, ?)";
        PreparedStatement stat = connection.prepareStatement(query);
        stat.setString(1, house.getName());
        stat.setInt(2, house.getYear());
        stat.setLong(3, house.getNumberOfFlatsOnFloor());

        ResultSet result = stat.executeQuery();
        if (result.next()){
            return result.getInt(1);
        }
        return -1;
    }

    public int updateFlatById(Flat flat, Integer id) throws SQLException {
        String query = "UPDATE flats SET name=?, x=?, y=?," +
                " date=?, area=?, numb_of_rooms=?," +
                " height=?, furnish=?, transport=?, house=? WHERE id=?";

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

        if (flat.getHouse() != null){
            int houseId = insertHouse(flat.getHouse(), connection);
            stat.setInt(10, houseId);
        } else stat.setNull(10, Types.OTHER);

        stat.setInt(11, id);

        int result = stat.executeUpdate();
        connection.commit();
        return result;
    }

    private int getLastFlatId(Connection connection) throws SQLException {
        String query = "SELECT last_value FROM flats_id_seq;";
        ResultSet result = connection.createStatement().executeQuery(query);

        if (result.next()){
            return result.getInt(1);
        } else {
            throw new SQLException("Не удалось получить flat id");
        }
    }
}
