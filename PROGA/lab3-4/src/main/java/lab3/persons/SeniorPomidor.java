package lab3.persons;

import lab3.enumerations.HappinessLevel;
import lab3.interfaces.CanCapture;
import lab3.interfaces.CanRejoice;
import lab3.interfaces.CanRelease;
import lab3.places.Place;

public class SeniorPomidor extends Person implements CanCapture, CanRelease, CanRejoice {
    public SeniorPomidor(String name){
        super(name);
    }

    @Override
    public String capture(Person person) {
        return this.getName() + " захватил " + person;
    }

    @Override
    public String release(Person person, Place place) {
        return this.getName() + " отпустил " + person.toString() + " по " + place.toString();
    }

    @Override
    public String rejoice() {
        this.setHappinessLevel(HappinessLevel.HAPPY);
        return this.getName() + " обрадовался";
    }
}
