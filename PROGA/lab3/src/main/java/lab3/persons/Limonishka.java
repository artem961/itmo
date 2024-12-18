package lab3.persons;

import lab3.interfaces.CanBring;
import lab3.items.Item;

public class Limonishka extends Person implements CanBring {
    public Limonishka(String name) {
        super(name);
    }

    @Override
    public String bring(Item item) {
        return "приносил " + item.toString();
    }
}
