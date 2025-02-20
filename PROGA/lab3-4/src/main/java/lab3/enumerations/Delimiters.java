package lab3.enumerations;

public enum Delimiters {
    COMMA(","),
    AND("и"),
    FIRSTLY_BECAUSE("во-первых, потому что"),
    SECONDLY_BECAUSE("во-вторых, потому что");

    private String text;
    private Delimiters(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
