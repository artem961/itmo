package lab5.collection.interfaces;

import lab5.collection.exceptions.ValidationException;

/**
 * Интерфейс возможности валидации данных в классе.
 */
public interface Validatable {
    /**
     * Валидировать данные.
     * @throws ValidationException
     */
    void validate() throws ValidationException;
}
