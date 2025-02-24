package lab5.collection.utils;

/**
 * Генератор ID.
 */
public class IdGenerator {
    private Integer nextId;

    public IdGenerator() {
        this.nextId = 0;
    }

    /**
     * Выдаёт следующий ID
     *
     * @return ID
     */
    public Integer getNextId() {
        nextId++;
        return nextId;
    }
}
