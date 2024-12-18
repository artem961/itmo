package lab3.sentence;

public enum Delimiters {
    COMMA(","),
    DOT("."),
    BECAUSE("потому что"),
    FIRSTLY("во-первых"),
    SECONDLY("во-вторых");

    private String text;
    private Delimiters(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
