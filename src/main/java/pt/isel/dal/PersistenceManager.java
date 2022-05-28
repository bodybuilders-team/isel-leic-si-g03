package pt.isel.dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PersistenceManager {
    private static EntityManagerFactory emf;
    private static final ThreadLocal<EntityManager> threadLocalEm = ThreadLocal.withInitial(() -> null);

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

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null)
            emf = createEntityManagerFactory();

        return emf;
    }

    public static EntityManager getEntityManager() {
        if (threadLocalEm.get() == null) {
            EntityManager em = getEntityManagerFactory().createEntityManager();
            threadLocalEm.set(em);
        }

        return threadLocalEm.get();
    }

    public static void closeEntityManager() {
        if (threadLocalEm.get() == null)
            return;

        threadLocalEm.get().close();
        threadLocalEm.set(null);
    }

    public static void closeEntityManagerFactory() {
        if (emf == null)
            return;

        emf.close();
        emf = null;
    }

    public static void execute(Runnable runnable) {
        EntityManager em = getEntityManager();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            runnable.run();

            tx.commit();
        } catch (Exception e) {
            if(tx.isActive())
                tx.rollback();

            throw e;
        } finally {
            PersistenceManager.closeEntityManager();
        }
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(persistanceUnitName);
    }
}