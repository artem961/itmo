package lab3.persons;

import lab3.interfaces.CanCapture;
import lab3.interfaces.CanRelease;
import lab3.places.Place;

public class SeniorPomidor extends Person implements CanCapture, CanRelease {
    public SeniorPomidor(String name){
        super(name);
    }

    @Override
    public String capture(Person person) {
        return this + " захватил " + person;
    }

    @Override
    public String release(Person person, Place place) {
        return "отпустил " + person.toString();
    }
}
