package lab5.collection.models;

import lab5.collection.DumpManager;
import lab5.collection.exceptions.EmptyFieldException;
import lab5.collection.exceptions.FieldLowerThanValidException;
import lab5.collection.exceptions.NullFieldException;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.interfaces.Validatable;

import java.io.IOException;
import java.time.LocalDate;

public class Flat implements Comparable<Flat>, Validatable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    // private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
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
                House house) throws ValidationException { // нет Furnish и id
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.height = height;
        this.transport = transport;
        this.house = house;
        //  this.creationDate = LocalDate.now();
        validate();

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
                House house) throws ValidationException {
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.height = height;
        this.transport = transport;
        this.house = house;
        this.furnish = furnish;
        //  this.creationDate = LocalDate.now();
        validate();

    }

    //region setteres
    public void setId(Integer id) throws ValidationException {
        if (id == null) throw new NullFieldException("id");
        if (id <= 0) throw new FieldLowerThanValidException("id", 0);
        this.id = id;
    }

    public void setName(String name) throws ValidationException {
        if (name == null) throw new NullFieldException("name");
        if (name.isEmpty()) throw new EmptyFieldException("name");
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) throws ValidationException {
        if (coordinates == null) throw new NullFieldException("coordinates");
        this.coordinates = coordinates;
    }

    public void setArea(float area) throws ValidationException {
        if (area <= 0) throw new FieldLowerThanValidException("area", 0);
        this.area = area;
    }

    public void setNumberOfRooms(int numberOfRooms) throws ValidationException {
        if (numberOfRooms <= 0) throw new FieldLowerThanValidException("numberOfRooms", 0);
        this.numberOfRooms = numberOfRooms;
    }

    public void setHeight(long height) throws ValidationException {
        if (height <= 0) throw new FieldLowerThanValidException("height", 0);
        this.height = height;
    }

    public void setFurnish(Furnish furnish) {
        this.furnish = furnish;
    }

    public void setTransport(Transport transport) throws ValidationException {
        if (transport == null) throw new NullFieldException("transport");
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

    // public LocalDate getCreationDate() {
    //   return creationDate;
    //}

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
                //"\"creationDate\" = \"" + creationDate + "\", " +
                "\"area\": \"" + area + "\", " +
                "\"numberOfRooms\" = \"" + numberOfRooms + "\", " +
                "\"height\": \"" + height + "\", " +
                "\"furnish\": \"" + furnish + "\", " +
                "\"transport\": \"" + transport + "\", " +
                "\"house\": \"" + house + "\"" + "}";
    }

    @Override
    public void validate() throws ValidationException {
        if (id == null) throw new NullFieldException("id");
        if (id <= 0) throw new FieldLowerThanValidException("id", 0);

        if (name == null) throw new NullFieldException("name");
        if (name.isEmpty()) throw new EmptyFieldException("name");

        if (coordinates == null) throw new NullFieldException("coordinates");

        if (area <= 0) throw new FieldLowerThanValidException("area", 0);

        if (numberOfRooms <= 0) throw new FieldLowerThanValidException("numberOfRooms", 0);

        if (height <= 0) throw new FieldLowerThanValidException("height", 0);

        if (transport == null) throw new NullFieldException("transport");
    }
}
