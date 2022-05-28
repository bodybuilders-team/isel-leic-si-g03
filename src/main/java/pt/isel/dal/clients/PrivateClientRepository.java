package pt.isel.dal.clients;

import pt.isel.dal.Repository;
import pt.isel.model.clients.PrivateClient;


import static pt.isel.dal.PersistenceManager.getEntityManager;

public class PrivateClientRepository extends Repository<PrivateClient> {

    public PrivateClient getByName(String name) {
        return getEntityManager()
                .createQuery("SELECT pc FROM PrivateClient pc WHERE pc.name = :name", PrivateClient.class)
                .setParameter("name", name)
                .getSingleResult();
    }

}
