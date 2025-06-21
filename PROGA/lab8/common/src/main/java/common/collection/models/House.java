package common.collection.models;

import common.collection.exceptions.FieldLowerThanValidException;
import common.collection.exceptions.ValidationException;
import common.collection.interfaces.Validatable;
import lombok.Getter;

import java.io.Serializable;

/**
 * Дом.
 */
@Getter
public class House implements Validatable, Serializable {
    private String name; //Поле может быть null
    private int year; //Значение поля должно быть больше 0
    private Long numberOfFlatsOnFloor; //Значение поля должно быть больше 0

    public House(String name, int year, Long numberOfFlatsOnFloor) throws ValidationException {
        this.name = name;
        this.year = year;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
        validate();
    }


    @Override
    public void validate() throws ValidationException {
        Validator.validateName(name);
        Validator.validateYear(year);
        Validator.validateNumberOfFlatsOnFloor(numberOfFlatsOnFloor);
    }
    //endregion

    @Override
    public String toString() {
        return "House{\"name\": \"" + name + "\", " +
                "\"year\": " + year + ", " +
                "\"numberOfFlatsOnFloor\": " + numberOfFlatsOnFloor + "}";
    }

    public static class Validator {
        /**
         * Проверить валидность названия.
         *
         * @param name
         * @throws ValidationException
         */
        public static void validateName(String name) throws ValidationException {
        }

        /**
         * Проверить валидность года.
         *
         * @param year
         * @throws ValidationException
         */
        public static void validateYear(int year) throws ValidationException {
            if (year <= 0) throw new FieldLowerThanValidException("year", 0);
        }

        /**
         * Проверить валидность количества квартир на этаже.
         *
         * @param numberOfFlatsOnFloor
         * @throws ValidationException
         */
        public static void validateNumberOfFlatsOnFloor(Long numberOfFlatsOnFloor) throws ValidationException {
            if (numberOfFlatsOnFloor <= 0) throw new FieldLowerThanValidException("numberOfFlatsOnFloor", 0);
        }
    }
}
