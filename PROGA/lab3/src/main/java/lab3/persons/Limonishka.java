package lab3.persons;

import lab3.enumerations.Period;
import lab3.interfaces.CanBring;
import lab3.items.Item;

public class Limonishka extends Person implements CanBring {
    public Limonishka(String name) {
        super(name);
    }

    @Override
    public String bring(Item item, Period period) {
        return this.toString() + " " + period.getText() + " приносил " + item.toString();
    }

    @Override
    public String toString() {
        return this.profession + " " + this.name;
    }
}
