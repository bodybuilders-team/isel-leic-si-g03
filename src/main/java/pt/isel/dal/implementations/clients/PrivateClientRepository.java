package pt.isel.dal.implementations.clients;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;

import java.util.Optional;

import pt.isel.dal.Repository;
import pt.isel.model.clients.Client;
import pt.isel.model.clients.PrivateClient;


/**
 * Repository for PrivateClient.
 */
public class PrivateClientRepository extends Repository<PrivateClient> {

    public PrivateClientRepository(EntityManager em) {
        super(em);
    }

    /**
     * Gets the PrivateClient with the given name.
     *
     * @param name the name of the PrivateClient
     * @return the PrivateClient with the given name
     */
    public Optional<PrivateClient> getByName(String name) {
        return em
                .createQuery("SELECT pc FROM PrivateClient pc WHERE pc.name = :name", PrivateClient.class)
                .setParameter("name", name)
                .setLockMode(LockModeType.PESSIMISTIC_READ)
                .getResultStream()
                .findFirst();
    }

    /**
     * Inserts the given PrivateClient.
     *
     * @param client the PrivateClient to be inserted
     */
    public void add(PrivateClient client) {
        Query query = em.createNativeQuery("CALL insert_private_client(?,?,?,?::CHAR,?,?::INTEGER)");

        query.setParameter(1, client.getCitizenCardNumber());
        query.setParameter(2, client.getName());
        query.setParameter(3, client.getPhoneNumber());
        query.setParameter(4, client.getNif());
        query.setParameter(5, client.getAddress());

        Client referral = client.getReferral();
        Integer referralId = (referral == null) ? null : referral.getId();
        query.setParameter(6, referralId);

        query.executeUpdate();
    }

    /**
     * Removes the given PrivateClient.
     *
     * @param client the PrivateClient to be removed
     */
    public void remove(PrivateClient client) {
        Query query = em.createNativeQuery("CALL remove_private_client(?::INTEGER)");

        query.setParameter(1, client.getId());
        query.executeUpdate();
    }

    /**
     * Updates the given PrivateClient.
     *
     * @param client the PrivateClient to be updated
     */
    public void update(PrivateClient client) {
        Query query = em.createNativeQuery("CALL update_private_client(?::INTEGER,?,?::CHAR,?,?,?::INTEGER)");

        query.setParameter(1, client.getId());
        query.setParameter(2, client.getCitizenCardNumber());
        query.setParameter(3, client.getNif());
        query.setParameter(4, client.getName());
        query.setParameter(5, client.getAddress());

        Client referral = client.getReferral();
        Integer referralId = (referral == null) ? null : referral.getId();
        query.setParameter(6, referralId);

        query.executeUpdate();
    }
}
