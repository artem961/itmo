package lab5.collection.models;

import lab5.collection.exceptions.FieldLowerThanValidException;
import lab5.collection.exceptions.NullFieldException;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.interfaces.Validatable;

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

    //region setters
    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) throws ValidationException {
        if (year <= 0) throw new FieldLowerThanValidException("year", 0);
        this.year = year;
    }

    public void setNumberOfFlatsOnFloor(Long numberOfFlatsOnFloor) throws ValidationException {
        if (numberOfFlatsOnFloor <= 0) throw new FieldLowerThanValidException("numberOfFlatsOnFloor", 0);
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
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
        return "House{\"name\": " + name + ", " +
                "\"year\": \"" + year + "\", " +
                "\"numberOfFlatsOnFloor\": \"" + numberOfFlatsOnFloor + "\"" + "}";
    }

    @Override
    public void validate() throws ValidationException {
        if (year <= 0) throw new FieldLowerThanValidException("year", 0);
        if (numberOfFlatsOnFloor <= 0) throw new FieldLowerThanValidException("numberOfFlatsOnFloor", 0);
    }
}
