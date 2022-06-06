package pt.isel.dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.sessions.DatabaseLogin;

/**
 * Represents a persistence manager.
 * A persistence manager is a class that provides functions for managing the persistence context.
 */
public class PersistenceManager {


    /**
     * Name of the persistence unit.
     */
    private static final String persistanceUnitName;


    public static class Session implements Closeable {
        private final EntityManagerFactory emf;
        private final EntityManager em;

        public Session() {
            this.emf = Persistence.createEntityManagerFactory(persistanceUnitName);
            this.em = emf.createEntityManager();
        }

        @Override
        public void close() {
            getEm().close();
            emf.close();
        }

        public EntityManager getEm() {
            return em;
        }
    }

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
     * Executes a transaction, given a function.
     *
     * @param fun the runnable to be executed
     */
    public static <T> T execute(Function<EntityManager, T> fun) {
        Session session = new Session();

        return execute(session, fun);
    }

    /**
     * Executes a transaction, given a consumer.
     *
     * @param consumer the runnable to be executed
     */
    public static void execute(Consumer<EntityManager> consumer) {
        Session session = new Session();
        Function<EntityManager, Void> f = em -> {
            consumer.accept(em);
            return null;
        };

        execute(session, f);
    }

    /**
     * Executes a transaction, given a runnable and an isolation level.
     *
     * @param transactionIsolationLevel the isolation level of the transaction
     * @param fun                       the runnable to be executed
     */
    public static <T> T executeWithIsolationLevel(int transactionIsolationLevel, Function<EntityManager, T> fun) {
        Session session = new Session();

        DatabaseLogin databaseLogin = getDatabaseLogin(session.getEm());
        databaseLogin.setTransactionIsolation(transactionIsolationLevel);

        return execute(session, fun);
    }

    /**
     * Executes a transaction, given a runnable and an isolation level.
     *
     * @param transactionIsolationLevel the isolation level of the transaction
     * @param fun                       the runnable to be executed
     */
    public static void executeWithIsolationLevel(int transactionIsolationLevel, Consumer<EntityManager> fun) {
        Session session = new Session();

        DatabaseLogin databaseLogin = getDatabaseLogin(session.getEm());
        databaseLogin.setTransactionIsolation(transactionIsolationLevel);

        Function<EntityManager, Void> f = em -> {
            fun.accept(em);
            return null;
        };

        execute(session, f);
    }


    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    private static <T> T execute(Session session, Function<EntityManager, T> fun) {
        EntityManager em = session.getEm();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            T value = fun.apply(em);

            tx.commit();

            return value;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();

            throw e;
        } finally {
            session.close();
        }
    }

    private static DatabaseLogin getDatabaseLogin(EntityManager em) {
        org.eclipse.persistence.sessions.Session session = ((EntityManagerImpl) em).getSession();
        return (DatabaseLogin) (session).getDatasourceLogin();
    }

}