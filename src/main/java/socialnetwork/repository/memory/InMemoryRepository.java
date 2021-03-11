package socialnetwork.repository.memory;
import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exceptions.RepositoryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private Validator<E> validator;
    Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null)
            throw new RepositoryException("ID cannot be null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null)
            throw new RepositoryException("Entity cannot be null");
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null)
            throw new RepositoryException("ID cannot be null");
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
       if(entity == null)
           throw new RepositoryException("Entity cannot be null");
       validator.validate(entity);
       return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (K, V)->entity));
    }
}
