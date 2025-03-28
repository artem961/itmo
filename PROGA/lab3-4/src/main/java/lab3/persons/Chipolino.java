package lab3.persons;

import lab3.enumerations.HappinessLevel;
import lab3.enumerations.HungerLevel;
import lab3.interfaces.CanBeSend;
import lab3.interfaces.CanEat;
import lab3.interfaces.Lockable;
import lab3.interfaces.NotSee;
import lab3.items.Food;
import lab3.places.Place;

public class Chipolino extends Person implements Lockable, CanBeSend, CanEat, NotSee {
    public Chipolino(String name) {
        super(name);
    }

    @Override
    public String lock(Place place) {
        this.setHappinessLevel(HappinessLevel.SAD);
        return "заперли в " + place.toString();
    }

    @Override
    public String send(Place place, Person attendant) {
        return this.getName() + " отправили в " + place.toString() + " в сопровождении " + attendant.toString();
    }

    @Override
    public String eat(Food food) {
        this.setHungerLevel(HungerLevel.SATISFIED);
        return this.getName() + " съел " + food.toString();
    }

    @Override
    public String notSee() {
        return "не глядя";
    }
}
