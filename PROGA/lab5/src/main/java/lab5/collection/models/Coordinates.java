package lab5.collection.models;

import lab5.collection.exceptions.FieldLowerThanValidException;
import lab5.collection.exceptions.NullFieldException;

import lab5.collection.exceptions.ValidationException;
import lab5.collection.interfaces.Validatable;

/**
 * Координаты.
 */
public class Coordinates implements Validatable {
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