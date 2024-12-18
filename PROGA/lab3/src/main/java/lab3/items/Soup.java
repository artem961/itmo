package lab3.items;

import lab3.exceptions.TooFewArguments;

public class Soup extends Food{
    protected Ingridients content[];

    public Soup(Ingridients... content){
        super("похлёбка");

        try {
            this.setContent(content);
        } catch (TooFewArguments e) {
            System.out.println(e.getMessage());
        }
    }

    public Ingridients[] getContent() {
        return content;
    }

    protected void setContent(Ingridients... content) throws TooFewArguments {
        if (content.length < 1){
            throw new TooFewArguments("Число аргументов не меньше 1!");
        }
        this.content = content;
    }

    @Override
    public String toString() {
        String text = this.getName() + " из:";
        try {
            for (Ingridients ingr : content) {
                text += " " + ingr.getText();
            }
        }catch (NullPointerException e){
            return text + " ничего";
        }
        return text;
    }
}
