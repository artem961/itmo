package lab3.items;

import java.util.Objects;

public abstract class Item {
    protected String name;

    public Item(String name){
        this.name = name;
    }

    // region Setters and Getters
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    // endregion

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
