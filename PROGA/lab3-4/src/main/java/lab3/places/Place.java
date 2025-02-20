package lab3.places;

import lab3.enumerations.Luminocity;
import lab3.enumerations.RoomType;

import java.util.Objects;

public abstract class Place {
    protected String name;
    protected RoomType roomType;
    protected Luminocity luminocity;

    public Place(String name, RoomType roomType){
        this.name = name;
        this.roomType = roomType;
        this.luminocity = Luminocity.HASLIGHT;
    }

    // region Setters and Getters
    public void setName(String name) {
        this.name = name;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setLuminocity(Luminocity luminocity) {
        this.luminocity = luminocity;
    }

    public String getName() {
        return name;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public Luminocity getLuminocity() {
        return luminocity;
    }
    // endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(name, place.name) && roomType == place.roomType && luminocity == place.luminocity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, roomType, luminocity);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
