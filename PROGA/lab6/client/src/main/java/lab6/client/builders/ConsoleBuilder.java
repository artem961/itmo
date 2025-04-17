package lab6.client.builders;

/**
 * Интерфейс для сборки объектов через консоль.
 */
public interface ConsoleBuilder {
    Object build();

    /**
     * Получить Integer от пользователя.
     *
     * @param message
     * @return
     */
    Integer inputInteger(String message);

    /**
     * Получить Float от пользователя.
     *
     * @param message
     * @return
     */
    Float inputFloat(String message);

    /**
     * Получить Double от пользователя.
     *
     * @param message
     * @return
     */
    Double inputDouble(String message);

    /**
     * Получить Long от пользователя.
     *
     * @param message
     * @return
     */
    Long inputLong(String message);

    /**
     * Получить String от пользователя.
     *
     * @param message
     * @return
     */
    String inputString(String message);

    /**
     * Получить объект перечисления.
     * @return
     * @param <E>
     */
    <E extends Enum<E>> E inputEnum(String message, Class<E> enumeration);
}
