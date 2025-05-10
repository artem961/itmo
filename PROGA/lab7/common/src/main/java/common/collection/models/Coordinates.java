package common.collection.models;

import common.collection.exceptions.FieldLowerThanValidException;
import common.collection.exceptions.NullFieldException;

import common.collection.exceptions.ValidationException;
import common.collection.interfaces.Validatable;

import java.io.Serializable;

/**
 * Координаты.
 */
public class Coordinates implements Validatable, Serializable {
    private Float x; //Значение поля должно быть больше -621, Поле не может быть null
    private double y;

    public Coordinates(Float x, double y) throws ValidationException {
        this.x = x;
        this.y = y;
        validate();
    }

    //region validation
    public static class Validator {
        /**
         * Проверить валидность x.
         *
         * @param x
         * @throws ValidationException
         */
        public static void validateX(Float x) throws ValidationException {
            if (x == null) throw new NullFieldException("x");
            if (x <= -621) throw new FieldLowerThanValidException("x", -621);
        }

        /**
         * Проверить валидность y.
         *
         * @param y
         * @throws ValidationException
         */
        public static void validateY(double y) throws ValidationException {

        }
    }

    @Override
    public void validate() throws ValidationException {
        Validator.validateX(x);
        Validator.validateY(y);
    }
    //endregion

    //region getters
    public Float getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    //endregion


    @Override
    public String toString() {
        return "Coordinates{\"x\": " + x + ", " +
                "\"y\": " + y + "}";
    }
}