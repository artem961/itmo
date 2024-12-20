package lab3.items;

public abstract class Food extends Item{
    public Food(String name){
        super(name);
    }

    public abstract String goBad();
}
