package lab5.collection.interfaces;

import lab5.collection.exceptions.ValidationException;

public interface Validatable {
    public void validate() throws ValidationException;
}
