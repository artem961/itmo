package lab5.collection.interfaces;

import lab5.collection.exceptions.ValidationException;

/**
 * Интерфейс возможности валидации данных в классе.
 */
public interface Validatable {
    void validate() throws ValidationException;
}
