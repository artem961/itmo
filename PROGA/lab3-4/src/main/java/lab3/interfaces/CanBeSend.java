package lab3.interfaces;

import lab3.persons.Person;
import lab3.places.Place;

public interface CanBeSend {
    public String send(Place place, Person attendant);
}
