package lab5.collection.models;

import lab5.collection.exceptions.EmptyFieldException;
import lab5.collection.exceptions.FieldLowerThanValidException;
import lab5.collection.exceptions.NullFieldException;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.interfaces.Validatable;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Квартира.
 */
public class Flat implements Comparable<Flat>, Validatable {
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
     * Конструктор без furnish.
     *
     * @param name
     * @param coordinates
     * @param area
     * @param numberOfRooms
     * @param height
     * @param transport
     * @param house
     * @throws ValidationException
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
        this.creationDate = LocalDate.now();
        //validate();
    }

    /**
     * Перегрузка конструктора с furnish
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
        this.creationDate = LocalDate.now();
        //validate();
    }

    //region setteres
    public void setId(Integer id) throws ValidationException {
        if (id == null) throw new NullFieldException("id");
        if (id <= 0) throw new FieldLowerThanValidException("id", 0);
        this.id = id;
    }

    public void setCreationDate() {
        this.creationDate = LocalDate.now();
    }
    // endregion

    //region validation
    public static class Validator {
        /**
         * Проверить валидность id.
         *
         * @param id
         * @throws ValidationException
         */
        public static void validateId(Integer id) throws ValidationException {
            if (id == null) throw new NullFieldException("id");
            if (id <= 0) throw new FieldLowerThanValidException("id", 0);
        }

        /**
         * Проверить валидность имени.
         *
         * @param name
         * @throws ValidationException
         */
        public static void validateName(String name) throws ValidationException {
            if (name == null) throw new NullFieldException("name");
            if (name.isEmpty()) throw new EmptyFieldException("name");
        }

        /**
         * Проверить валидность координат.
         *
         * @param coordinates
         * @throws ValidationException
         */
        public static void validateCoordinates(Coordinates coordinates) throws ValidationException {
            if (coordinates == null) throw new NullFieldException("coordinates");
        }

        /**
         * Проверить валидность даты создания.
         *
         * @throws ValidationException
         */
        public static void validateCreationDate() throws ValidationException {
        }

        /**
         * Проверить валидность площади.
         *
         * @param area
         * @throws ValidationException
         */
        public static void validateArea(float area) throws ValidationException {
            if (area <= 0) throw new FieldLowerThanValidException("area", 0);
        }

        /**
         * Проверить валидность количества комнат.
         *
         * @param numberOfRooms
         * @throws ValidationException
         */
        public static void validateNumberOfRooms(int numberOfRooms) throws ValidationException {
            if (numberOfRooms <= 0) throw new FieldLowerThanValidException("numberOfRooms", 0);
        }

        /**
         * Проверить валидность высоты.
         *
         * @param height
         * @throws ValidationException
         */
        public static void validateHeight(long height) throws ValidationException {
            if (height <= 0) throw new FieldLowerThanValidException("height", 0);
        }

        /**
         * Проверить валидность мебели.
         *
         * @param furnish
         * @throws ValidationException
         */
        public static void validateFurnish(Furnish furnish) throws ValidationException {
        }

        /**
         * Проверить валидность транспорта.
         *
         * @param transport
         * @throws ValidationException
         */
        public static void validateTransport(Transport transport) throws ValidationException {
            if (transport == null) throw new NullFieldException("transport");
        }

        /**
         * Проверить валидность дома.
         *
         * @param house
         * @throws ValidationException
         */
        public static void validateHouse(House house) throws ValidationException {

        }
    }

    @Override
    public void validate() throws ValidationException {
        Validator.validateId(id);
        Validator.validateName(name);
        Validator.validateCoordinates(coordinates);
        Validator.validateArea(area);
        Validator.validateNumberOfRooms(numberOfRooms);
        Validator.validateHeight(height);
        Validator.validateTransport(transport);
    }
    //endregion

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
        return Float.compare(area, o.getArea());
    }

    @Override
    public String toString() {
        return "Flat{\"id\": " + id + ", " +
                "\"name\": \"" + name + "\", " +
                "\"coordinates\": \"" + coordinates + "\", " +
                "\"creationDate\" = \"" + creationDate + "\", " +
                "\"area\": " + area + ", " +
                "\"numberOfRooms\" = " + numberOfRooms + ", " +
                "\"height\": " + height + ", " +
                "\"furnish\": \"" + furnish + "\", " +
                "\"transport\": \"" + transport + "\", " +
                "\"house\": \"" + house + "\"" + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o;
        return Float.compare(area, flat.area) == 0 && numberOfRooms == flat.numberOfRooms && height == flat.height && Objects.equals(id, flat.id) && Objects.equals(name, flat.name) && Objects.equals(coordinates, flat.coordinates) && Objects.equals(creationDate, flat.creationDate) && furnish == flat.furnish && transport == flat.transport && Objects.equals(house, flat.house);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, area, numberOfRooms, height, furnish, transport, house);
    }
}
