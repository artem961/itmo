package common.collection.models;

import common.collection.exceptions.EmptyFieldException;
import common.collection.exceptions.FieldLowerThanValidException;
import common.collection.exceptions.NullFieldException;
import common.collection.exceptions.ValidationException;
import common.collection.interfaces.Validatable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Квартира.
 */

@Getter
@Setter
@EqualsAndHashCode
public class Flat implements Comparable<Flat>, Validatable, Serializable {
    @NonNull
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @NonNull
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NonNull
    private Coordinates coordinates; //Поле не может быть null
    @NonNull
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float area; //Значение поля должно быть больше 0
    private int numberOfRooms; //Значение поля должно быть больше 0
    private long height; //Значение поля должно быть больше 0
    private Furnish furnish; //Поле может быть null
    @NonNull
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
    }

    @Override
    public void validate() throws ValidationException {
        //Validator.validateId(id);
        Validator.validateName(name);
        Validator.validateCoordinates(coordinates);
        Validator.validateArea(area);
        Validator.validateNumberOfRooms(numberOfRooms);
        Validator.validateHeight(height);
        Validator.validateTransport(transport);
    }
    //endregion

    @Override
    public int compareTo(Flat o) {
        return this.getName().compareTo(o.getName());
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
}
