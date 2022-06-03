package pt.isel.dal.clients;

import pt.isel.dal.Repository;
import pt.isel.model.clients.PrivateClient;


import static pt.isel.dal.PersistenceManager.getEntityManager;

/**
 * Repository for PrivateClient.
 */
public class PrivateClientRepository extends Repository<PrivateClient> {

    /**
     * Gets the PrivateClient with the given name.
     *
     * @param name the name of the PrivateClient
     * @return the PrivateClient with the given name
     */
    public PrivateClient getByName(String name) {
        return getEntityManager()
                .createQuery("SELECT pc FROM PrivateClient pc WHERE pc.name = :name", PrivateClient.class)
                .setParameter("name", name)
                .getSingleResult();
    }

}
