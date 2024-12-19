package lab3.enumerations;

public enum HungerLevel {
    SATISFIED("сытый"),
    NEUTRAL("не голоден"),
    HUNGRY("голоден");

    private String text;

    private HungerLevel(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
