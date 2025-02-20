package lab3.enumerations;

public enum HappinessLevel {
    HAPPY("cчастлив"),
    NEUTRAL("нейтрален"),
    SAD("грустняшка");

    private String text;

    private HappinessLevel(String text){
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
