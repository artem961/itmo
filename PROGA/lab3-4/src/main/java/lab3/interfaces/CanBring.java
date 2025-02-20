package lab3.interfaces;

import lab3.enumerations.Period;
import lab3.items.Item;

public interface CanBring {
    public String bring(Item item, Period period);
}
