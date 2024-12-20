package lab3.enumerations;

public enum Luminocity {
    HASLIGHT("всегда есть свет"),
    GLUMLY("мрачно"),
    NOLIGHT("никогда нет света");

    private String text;

    private Luminocity(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
