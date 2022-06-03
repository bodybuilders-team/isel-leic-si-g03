package pt.isel.dal;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import pt.isel.GenericTypeSolver;


import static pt.isel.dal.PersistenceManager.getEntityManager;

/**
 * Represents a repository for a specific entity.
 * A repository is a class that provides CRUD operations for a specific entity collection.
 *
 * @param <T> the type of the entity
 */
public abstract class Repository<T> {

    /**
     * Generic type of the entity.
     */
    private final Class<T> genericType;

    /**
     * Mapper for the entity.
     */
    private final Mapper<T> mapper;


    /**
     * Creates a new repository for the specified entity type.
     */
    @SuppressWarnings("unchecked")
    public Repository() {
        this.genericType = (Class<T>) GenericTypeSolver.getTypeArgument(getClass());
        this.mapper = new Mapper<>(genericType) {
        };
    }

    /**
     * Creates a new instance of the repository, given the generic type of the entity.
     *
     * @param genericType the generic type of the entity
     */
    public Repository(Class<T> genericType) {
        this.genericType = genericType;
        this.mapper = new Mapper<>(genericType) {
        };
    }

    /**
     * Gets all entities.
     *
     * @return list with all entities
     */
    public List<T> getAll() {
        return getEntityManager()
                .createQuery("SELECT entity FROM " + genericType.getSimpleName() + " entity", genericType)
                .getResultList();
    }

    /**
     * Gets an entity by its id.
     *
     * @param id the id of the entity to be read
     * @return the entity with the given id
     */
    public T getById(int id) {
        return mapper.read(id);
    }

    /**
     * Gets an entity given a predicate.
     *
     * @param predicate the predicate to be used
     * @return an optional discribing the first entity that matches the predicate,
     * or an empty optional if no entity matches
     */
    public Optional<T> get(Predicate<T> predicate) {
        return getEntityManager()
                .createQuery("SELECT entity FROM " + genericType.getSimpleName() + " entity", genericType)
                .getResultStream()
                .filter(predicate)
                .findFirst();
    }

    /**
     * Deletes all entities.
     */
    public void deleteAll() {
        getEntityManager()
                .createQuery("DELETE FROM " + genericType.getSimpleName()).executeUpdate();
    }
}
