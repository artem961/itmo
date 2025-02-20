package lab5.collection.models;

import lab5.collection.exceptions.FieldLowerThanValidException;
import lab5.collection.exceptions.NullFieldException;

import lab5.collection.exceptions.ValidationException;
import lab5.collection.interfaces.Validatable;

public class Coordinates implements Validatable{
    private Float x; //Значение поля должно быть больше -621, Поле не может быть null
    private double y;

    public Coordinates(Float x, double y) throws ValidationException{
        this.x = x;
        this.y = y;
        validate();
    }

    //region setters
    private void setX(Float x) throws ValidationException{
        if (x <= -621) throw new FieldLowerThanValidException("x", -621);
        if (x == null) throw new NullFieldException("x");
        this.x = x;
    }

    private void setY(double y) {
        this.y = y;
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
    public void validate() throws ValidationException{
        if (x <= -621) {
            this.setX(0f);
            throw new FieldLowerThanValidException("x", -621);
        }
        if (x == null) {
            this.setX(0f);
            throw new NullFieldException("x");
        }
    }

    @Override
    public String toString() {
        return "Coordinates{\"x\": " + x + ", " +
                "\"y\": \"" + y + "\"" + "}";
    }
}