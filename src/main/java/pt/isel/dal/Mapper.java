package pt.isel.dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import pt.isel.utils.GenericTypeSolver;

/**
 * Represents a mapper for a specific entity.
 * A mapper is a class that provides CRUD operations for a specific entity.
 *
 * @param <T> the type of the entity
 */
public abstract class Mapper<T> {

    /**
     * Entity manager of the entity.
     */
    protected final EntityManager em;
    /**
     * Generic type of the entity.
     */
    private final Class<T> genericType;

    /**
     * Creates a new mapper for the specified entity type.
     */
    @SuppressWarnings("unchecked")
    public Mapper(EntityManager em) {
        this.genericType = (Class<T>) GenericTypeSolver.getTypeArgument(getClass());
        this.em = em;
    }

    /**
     * Creates a new instance of the mapper, given the generic type of the entity.
     *
     * @param genericType the generic type of the entity
     */
    public Mapper(EntityManager em, Class<T> genericType) {
        this.genericType = genericType;
        this.em = em;
    }

    /**
     * Creates a new entity.
     *
     * @param entity the entity to be created
     */
    public void create(T entity) {
        em.persist(entity);
    }

    /**
     * Reads an entity by its id.
     *
     * @param id the id of the entity to be read
     * @return the entity with the given id
     */
    public Optional<T> read(int id) {
        return Optional.ofNullable(em.find(genericType, id, LockModeType.PESSIMISTIC_READ));
    }

    /**
     * Updates an entity.
     *
     * @param entity the entity to be updated
     */
    public T update(T entity) {
        em.flush();
        em.lock(entity, LockModeType.PESSIMISTIC_WRITE);
        return em.merge(entity);
    }

    /**
     * Deletes an entity.
     *
     * @param entity the entity to be deleted
     */
    public void delete(T entity) {
        em.remove(entity);
    }

}
