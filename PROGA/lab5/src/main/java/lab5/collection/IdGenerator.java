package lab5.collection;

public class IdGenerator {
    private Integer nextId;

    public IdGenerator(){
        this.nextId = 0;
    }

    public Integer getNextId(){
        nextId++;
        return nextId;
    }
}
