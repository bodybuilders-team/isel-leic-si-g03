package pt.isel.dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * Represents a persistence manager.
 * A persistence manager is a class that provides functions for managing the persistence context.
 */
public class PersistenceManager {

    /**
     * Entity manager factory.
     */
    private static EntityManagerFactory emf;

    /**
     * Entity manager stored in a thread local variable.
     */
    private static final ThreadLocal<EntityManager> threadLocalEm = ThreadLocal.withInitial(() -> null);

    /**
     * Name of the persistence unit.
     */
    private static final String persistanceUnitName;

    static {
        Properties props = new Properties();
        InputStream propsStream = PersistenceManager.class.getResourceAsStream("/persistence.properties");
        try {
            props.load(propsStream);

            persistanceUnitName = props.getProperty("persistence.unit.name");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the entity manager factory.
     * If the entity manager factory is not set, it creates a new one.
     *
     * @return the entity manager factory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null)
            emf = Persistence.createEntityManagerFactory(persistanceUnitName);

        return emf;
    }

    /**
     * Gets the entity manager.
     * If the entity manager is not set, it creates a new one.
     *
     * @return the entity manager
     */
    public static EntityManager getEntityManager() {
        if (threadLocalEm.get() == null) {
            EntityManager em = getEntityManagerFactory().createEntityManager();
            threadLocalEm.set(em);
        }

        return threadLocalEm.get();
    }

    /**
     * Close the entity manager factory.
     * If the entity manager factory is not set, it does nothing.
     */
    public static void closeEntityManagerFactory() {
        if (emf == null)
            return;

        emf.close();
        emf = null;
    }

    /**
     * Close the entity manager.
     * If the entity manager is not set, it does nothing.
     */
    public static void closeEntityManager() {
        if (threadLocalEm.get() == null)
            return;

        threadLocalEm.get().close();
        threadLocalEm.set(null);
    }

    /**
     * Executes a transaction, given a runnable.
     *
     * @param consumer the runnable to be executed
     */
    public static void execute(Consumer<EntityManager> consumer) {
        EntityManager em = getEntityManager();
        //Create entity manager factory per thread
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            consumer.accept(em);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();

            throw e;
        } finally {
            PersistenceManager.closeEntityManager();
            PersistenceManager.closeEntityManagerFactory();
        }
    }
}