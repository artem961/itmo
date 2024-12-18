package lab3.persons;

import java.util.Objects;

public abstract class Person {
    protected String name;
    protected String profession;
    protected int happinessLevel;
    protected int hungerLevel;

    public Person(String name){
        this.name = name;
    }

    // region Setters and Getters

    public String getName(){
        return this.name;
    }

    public String getProfession(){
        return this.profession;
    }

    public int getHappinessLevel(){
        return this.happinessLevel;
    }

    public int getHungerLevel(){
        return this.hungerLevel;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setProfession(String profeccion){
        this.profession = profeccion;
    }

    public void setHappinessLevel(int happinessLevel){
        this.happinessLevel = happinessLevel;
    }

    public void setHungerLevel(int hungerLevel){
        this.hungerLevel = hungerLevel;
    }
    // endregion

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        if (this.profession != null) return this.profession + " " + this.name;
        else return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return happinessLevel == person.happinessLevel && hungerLevel == person.hungerLevel && Objects.equals(name, person.name) && Objects.equals(profession, person.profession);
    }
}
