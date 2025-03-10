package lab5.collection.models;

/**
 * Мебель.
 */
public enum Furnish {
    DESIGNER,
    LITTLE,
    BAD,
    NONE;

    /**
     * Получить максимальное значение.
     *
     * @return
     */
    public static Furnish getMax() {
        return DESIGNER;
    }

    /**
     * Сравнить 2 значения перечисления.
     *
     * @param f1
     * @param f2
     * @return
     */
    public static int compare(Furnish f1, Furnish f2) {
        return -Integer.compare(f1.ordinal(), f2.ordinal());
    }
}