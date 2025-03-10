package lab5.collection.models;

import lab5.collection.exceptions.FieldLowerThanValidException;
import lab5.collection.exceptions.NullFieldException;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.interfaces.Validatable;

/**
 * Дом.
 */
public class House implements Validatable {
    private String name; //Поле может быть null
    private int year; //Значение поля должно быть больше 0
    private Long numberOfFlatsOnFloor; //Значение поля должно быть больше 0

    public House(String name, int year, Long numberOfFlatsOnFloor) throws ValidationException {
        this.name = name;
        this.year = year;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
        validate();
    }

    //region validation

    /**
     * Проверить валидность названия.
     *
     * @param name
     * @throws ValidationException
     */
    public static void ValidateName(String name) throws ValidationException {
    }

    /**
     * Проверить валидность года.
     *
     * @param year
     * @throws ValidationException
     */
    public static void ValidateYear(int year) throws ValidationException {
        if (year <= 0) throw new FieldLowerThanValidException("year", 0);
    }

    /**
     * Проверить валидность количества квартир на этаже.
     *
     * @param numberOfFlatsOnFloor
     * @throws ValidationException
     */
    public static void ValidateNumberOfFlatsOnFloor(Long numberOfFlatsOnFloor) throws ValidationException {
        if (numberOfFlatsOnFloor <= 0) throw new FieldLowerThanValidException("numberOfFlatsOnFloor", 0);
    }

    @Override
    public void validate() throws ValidationException {
        if (year <= 0) {
            this.year = 1;
            throw new FieldLowerThanValidException("year", 0);
        }
        if (numberOfFlatsOnFloor <= 0) {
            this.numberOfFlatsOnFloor = Long.valueOf(1);
            throw new FieldLowerThanValidException("numberOfFlatsOnFloor", 0);
        }
    }
    //endregion

    //region getters
    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public Long getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }
    //endregion


    @Override
    public String toString() {
        return "House{\"name\": \"" + name + "\", " +
                "\"year\": " + year + ", " +
                "\"numberOfFlatsOnFloor\": " + numberOfFlatsOnFloor + "}";
    }
}
