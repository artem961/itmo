package common.collection.interfaces;

import common.collection.exceptions.ValidationException;

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
