package lab3.persons;

import lab3.enumerations.HappinessLevel;
import lab3.enumerations.HungerLevel;

import java.util.Objects;

public abstract class Person {
    protected String name;
    protected String profession;
    protected HappinessLevel happinessLevel;
    protected HungerLevel hungerLevel;

    public Person(String name){
        this.name = name;
        this.profession = "тунеядец";
        this.happinessLevel = HappinessLevel.NEUTRAL;
        this.hungerLevel = HungerLevel.NEUTRAL;
    }

    // region Setters and Getters

    public String getName(){
        return this.name;
    }

    public String getProfession(){
        return this.profession;
    }

    public HappinessLevel getHappinessLevel(){
        return this.happinessLevel;
    }

    public HungerLevel getHungerLevel(){
        return this.hungerLevel;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setProfession(String profession){
        this.profession = profession;
    }

    public void setHappinessLevel(HappinessLevel happinessLevel){
        this.happinessLevel = happinessLevel;
    }

    public void setHungerLevel(HungerLevel hungerLevel){
        this.hungerLevel = hungerLevel;
    }
    // endregion

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return happinessLevel == person.happinessLevel && hungerLevel == person.hungerLevel && Objects.equals(name, person.name) && Objects.equals(profession, person.profession);
    }
}
