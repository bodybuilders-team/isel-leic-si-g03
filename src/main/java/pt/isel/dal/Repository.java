package pt.isel.dal;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import jakarta.persistence.LockModeType;
import pt.isel.utils.GenericTypeSolver;

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
    protected final Mapper<T> mapper;

    /**
     * Entity manager.
     */
    protected final EntityManager em;

    /**
     * Creates a new repository for the specified entity type.
     */
    @SuppressWarnings("unchecked")
    public Repository(EntityManager em) {
        this.genericType = (Class<T>) GenericTypeSolver.getTypeArgument(getClass());
        this.mapper = new Mapper<>(em, genericType) {
        };
        this.em = em;
    }

    /**
     * Creates a new instance of the repository, given the generic type of the entity.
     *
     * @param genericType the generic type of the entity
     */
    public Repository(EntityManager em, Class<T> genericType) {
        this.genericType = genericType;
        this.mapper = new Mapper<>(em, genericType) {
        };
        this.em = em;
    }

    /**
     * Adds the given entity to the repository.
     *
     * @param entity the entity to be added
     */
    public void add(T entity) {
        mapper.create(entity);
    }

    /**
     * Updates the given entity in the repository.
     *
     * @param entity the entity to be updated
     */
    public void update(T entity) {
        mapper.update(entity);
    }

    /**
     * Removes the given entity from the repository.
     *
     * @param entity the entity to be removed
     */
    public void remove(T entity) {
        mapper.delete(entity);
    }

    /**
     * Removes all entities.
     */
    public void removeAll() {
        em.createQuery("DELETE FROM " + genericType.getSimpleName()).executeUpdate();
    }

    /**
     * Gets an entity given a predicate.
     *
     * @param predicate the predicate to be used
     * @return an optional discribing the first entity that matches the predicate,
     * or an empty optional if no entity matches
     */
    public Optional<T> get(Predicate<T> predicate) {
        return em
                .createQuery("SELECT entity FROM " + genericType.getSimpleName() + " entity", genericType)
                .setLockMode(LockModeType.PESSIMISTIC_READ)
                .getResultStream()
                .filter(predicate)
                .findFirst();
    }

    /**
     * Gets all entities.
     *
     * @return list with all entities
     */
    public List<T> getAll() {
        return em
                .createQuery("SELECT entity FROM " + genericType.getSimpleName() + " entity", genericType)
                .setLockMode(LockModeType.PESSIMISTIC_READ)
                .getResultList();
    }

    /**
     * Gets an entity by its id.
     *
     * @param id the id of the entity to be read
     * @return the entity with the given id
     */
    public Optional<T> getById(int id) {
        return mapper.read(id);
    }
}
