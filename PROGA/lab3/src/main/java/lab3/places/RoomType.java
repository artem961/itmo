package lab3.places;

public enum RoomType {
    GROUND("наземная"),
    UNDERGROUND("подземная");

    private final String text;

    private RoomType(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
}
