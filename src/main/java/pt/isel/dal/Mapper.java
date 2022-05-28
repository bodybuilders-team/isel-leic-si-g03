package pt.isel.dal;

import pt.isel.GenericTypeSolver;


import static pt.isel.dal.PersistenceManager.getEntityManager;

public abstract class Mapper<T> {

    private final Class<T> genericType;

    @SuppressWarnings("unchecked")
    public Mapper() {
        this.genericType = (Class<T>) GenericTypeSolver.getTypeArgument(getClass());
    }

    public Mapper(Class<T> genericType) {
        this.genericType = genericType;
    }

    public void create(T client) {
        getEntityManager()
                .persist(client);
    }

    public T read(int id) {
        return getEntityManager()
                .find(genericType, id);
    }

    public void update(T client) {
        getEntityManager()
                .merge(client);
    }

    public void delete(T client) {
        getEntityManager()
                .remove(client);
    }

}
