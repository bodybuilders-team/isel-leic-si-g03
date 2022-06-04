package pt.isel.dal.implementations.clients;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.Optional;
import pt.isel.dal.Repository;
import pt.isel.model.clients.Client;
import pt.isel.model.clients.PrivateClient;


import static pt.isel.dal.PersistenceManager.getEntityManager;

/**
 * Repository for PrivateClient.
 */
public class PrivateClientRepository extends Repository<PrivateClient> {

    public PrivateClientRepository(EntityManager em) {
        super(em);
    }

    public PrivateClientRepository(EntityManager em, Class<PrivateClient> genericType) {
        super(em, genericType);
    }

    /**
     * Gets the PrivateClient with the given name.
     *
     * @param name the name of the PrivateClient
     * @return the PrivateClient with the given name
     */
    public Optional<PrivateClient> getByName(String name) {
        return getEntityManager()
                .createQuery("SELECT pc FROM PrivateClient pc WHERE pc.name = :name", PrivateClient.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    public void add(PrivateClient client) {
        EntityManager manager = getEntityManager();
        Query query = manager.createNativeQuery(
                "CALL insert_private_client(?,?,?,?::CHAR,?,?::INTEGER)");

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

    public void remove(PrivateClient client) {
        EntityManager manager = getEntityManager();
        Query query = manager.createNativeQuery(
                "CALL remove_private_client(?::INTEGER)");

        query.setParameter(1, client.getId());
        query.executeUpdate();
    }

    public void update(PrivateClient client) {
        EntityManager manager = getEntityManager();
        Query query = manager.createNativeQuery(
                "CALL update_private_client(?::INTEGER,?,?::CHAR,?,?,?::INTEGER)");

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
