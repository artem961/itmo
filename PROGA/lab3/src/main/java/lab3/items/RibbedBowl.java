package lab3.items;

public class RibbedBowl extends Thing{
    protected Food content;

    public RibbedBowl(String name) {
        super(name);
    }

    public void setContent(Food content) {
        this.content = content;
    }

    public Food getContent() {
        return content;
    }

    @Override
    public String toString() {
        try {
            return this.getName() + " с " + this.content.toString();
        } catch (NullPointerException e) {
            return "пустая " + this.getName();
        }
    }
}
