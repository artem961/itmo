package lab3.enumerations;

public enum Period {
    NEVER("никогда"),
    ONCE_A_DAY("один раз в день"),
    TWICE_A_DAY("дважды в день");
    private String text;

    private Period(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
