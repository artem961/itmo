package lab3.enumerations;

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

    @Override
    public String toString() {
        return this.getText();
    }
}
