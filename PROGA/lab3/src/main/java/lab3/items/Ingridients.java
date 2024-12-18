package lab3.items;

public enum Ingridients {
    BREAD("хлеб"),
    WATER("вода");

    private String text;

    private Ingridients(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
