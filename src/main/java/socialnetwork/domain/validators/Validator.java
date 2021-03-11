package socialnetwork.domain.validators;

public interface Validator<T> {

    /**
     * Function that validates an entity and throws ValidationExpresion if entity is INVALID.
     * @param entity
     * @throws ValidationException
     */
    void validate(T entity) throws ValidationException;
}