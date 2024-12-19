package lab3.enumerations;

public enum Luminocity {
    HASLIGHT("есть свет"),
    GLUMLY("мрачно"),
    NOLIGHT("нет света");

    private String text;

    private Luminocity(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
