package pt.isel.dal;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import pt.isel.GenericTypeSolver;


import static pt.isel.dal.PersistenceManager.getEntityManager;

public abstract class Repository<T> {
    private final Class<T> genericType;

    private final Mapper<T> mapper;

    public void deleteAll() {
        getEntityManager()
                .createQuery("DELETE FROM " + genericType.getSimpleName()).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public Repository() {
        this.genericType = (Class<T>) GenericTypeSolver.getTypeArgument(getClass());
        this.mapper = new Mapper<>(genericType){};
    }

    public Repository(Class<T> genericType) {
        this.genericType = genericType;
        this.mapper = new Mapper<>(genericType){};
    }

    public List<T> getAll() {
        return getEntityManager()
                .createQuery("SELECT entity FROM " + genericType.getSimpleName() + " entity", genericType)
                .getResultList();
    }

    public T getById(int id) {
        return mapper.read(id);
    }

    public Optional<T> get(Predicate<T> predicate) {
        return getEntityManager()
                .createQuery("SELECT entity FROM " + genericType.getSimpleName() + " entity", genericType)
                .getResultStream()
                .filter(predicate).findFirst();
    }
}
