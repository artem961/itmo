package lab3.persons;

import lab3.interfaces.Lockable;
import lab3.places.Place;

public class Vishenka extends Person implements Lockable {
    public Vishenka(String name) {
        super(name);
    }

    @Override
    public String lock(Place place) {
        return "заперли в " + place.toString();
    }
}
