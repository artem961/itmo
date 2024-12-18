package lab3.places;

public enum Luminocity {
    HASLIGHT("есть свет"),
    NOLIGHT("нет света");

    private String text;

    private Luminocity(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
