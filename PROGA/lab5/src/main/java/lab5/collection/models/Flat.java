package lab5.collection.models;

import lab5.collection.DumpManager;
import lab5.collection.exceptions.EmptyFieldException;
import lab5.collection.exceptions.FieldLowerThanValidException;
import lab5.collection.exceptions.NullFieldException;

import java.io.IOException;
import java.time.LocalDate;

public class Flat implements Comparable<Flat> {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float area; //Значение поля должно быть больше 0
    private int numberOfRooms; //Значение поля должно быть больше 0
    private long height; //Значение поля должно быть больше 0
    private Furnish furnish; //Поле может быть null
    private Transport transport; //Поле не может быть null
    private House house; //Поле может быть null

    /**
     * Конструктор без Furnish
     *
     * @param name
     * @param coordinates
     * @param area
     * @param numberOfRooms
     * @param height
     * @param transport
     * @param house
     */
    public Flat(String name,
                Coordinates coordinates,
                float area,
                int numberOfRooms,
                long height,
                Transport transport,
                House house) { // нет Furnish и id
        setName(name);
        setCoordinates(coordinates);
        setArea(area);
        setNumberOfRooms(numberOfRooms);
        setHeight(height);
        setHouse(house);
        this.creationDate = LocalDate.now();

    }

    /**
     * Перегрузка конструктора с Furnish
     *
     * @param name
     * @param coordinates
     * @param area
     * @param numberOfRooms
     * @param height
     * @param furnish
     * @param transport
     * @param house
     */
    public Flat(String name,
                Coordinates coordinates,
                float area,
                int numberOfRooms,
                long height,
                Furnish furnish,
                Transport transport,
                House house) {
        setName(name);
        setCoordinates(coordinates);
        setArea(area);
        setNumberOfRooms(numberOfRooms);
        setHeight(height);
        setHouse(house);
        setFurnish(furnish);
        this.creationDate = LocalDate.now();

    }

    //region setteres
    public void setId(Integer id) {
        if (id == null) throw new NullFieldException();
        if (id <= 0) throw new FieldLowerThanValidException(0);
        this.id = id;
    }

    public void setName(String name) {
        if (name == null) throw new NullFieldException();
        if (name.isEmpty()) throw new EmptyFieldException();
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) throw new NullFieldException();
        this.coordinates = coordinates;
    }

    public void setArea(float area) {
        if (area <= 0) throw new FieldLowerThanValidException(0);
        this.area = area;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        if (numberOfRooms <= 0) throw new FieldLowerThanValidException(0);
        this.numberOfRooms = numberOfRooms;
    }

    public void setHeight(long height) {
        if (height <= 0) throw new FieldLowerThanValidException(0);
        this.height = height;
    }

    public void setFurnish(Furnish furnish) {
        this.furnish = furnish;
    }

    public void setTransport(Transport transport) {
        if (transport == null) throw new NullFieldException();
        this.transport = transport;
    }

    public void setHouse(House house) {
        this.house = house;
    }
    // endregion

    //region getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public float getArea() {
        return area;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public House getHouse() {
        return house;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public long getHeight() {
        return height;
    }

    public Transport getTransport() {
        return transport;
    }
    //endregion

    @Override
    public int compareTo(Flat o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "Flat{\"id\": " + id + ", " +
                "\"name\": \"" + name + "\", " +
                "\"coordinates\": \"" + coordinates + "\", " +
                "\"creationDate\" = \"" + creationDate + "\", " +
                "\"area\": \"" + area + "\", " +
                "\"numberOfRooms\" = \"" + numberOfRooms + "\", " +
                "\"height\": \"" + height + "\", " +
                "\"furnish\": \"" + furnish + "\", " +
                "\"transport\": \"" + transport + "\", " +
                "\"house\": \"" + house + "\"" + "}";
    }
}
