package lab3.places;

import lab3.enumerations.RoomType;

public class Cell extends Place{
    public Cell(String name, RoomType roomType) {
        super(name, roomType);
    }

    @Override
    public String toString() {
        return this.roomType + " " + this.name;
    }
}
