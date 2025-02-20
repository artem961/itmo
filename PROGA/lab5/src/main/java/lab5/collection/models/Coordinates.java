package lab5.collection.models;

import lab5.collection.exceptions.FieldLowerThanValidException;
import lab5.collection.exceptions.NullFieldException;

import lab5.collection.interfaces.Validatable;

public class Coordinates{
    private Float x; //Значение поля должно быть больше -621, Поле не может быть null
    private double y;

    public Coordinates(Float x, double y) throws RuntimeException{
        setX(x);
        setY(y);
    }

    //region setters
    private void setX(Float x) {
        if (x <= -621) throw new FieldLowerThanValidException(-621);
        if (x == null) throw new NullFieldException();
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
    public String toString() {
        return "Coordinates{\"x\": " + x + ", " +
                "\"y\": \"" + y + "\"" + "}";
    }
}