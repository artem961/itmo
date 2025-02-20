package lab3.persons;

import lab3.enumerations.HappinessLevel;
import lab3.interfaces.Lockable;
import lab3.places.Place;

public class Vishenka extends Person implements Lockable {
    public Vishenka(String name) {
        super(name);
    }

    @Override
    public String lock(Place place) {
        this.setHappinessLevel(HappinessLevel.SAD);
        return this.getName() + " заперли в " + place.toString();
    }
}
