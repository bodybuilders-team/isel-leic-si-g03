package pt.isel.dal.implementations.clients;

import jakarta.persistence.EntityManager;
import pt.isel.dal.Mapper;
import pt.isel.model.clients.PrivateClient;

public class PrivateClientMapper extends Mapper<PrivateClient> {
    public PrivateClientMapper(EntityManager em) {
        super(em);
    }

    public PrivateClientMapper(EntityManager em, Class<PrivateClient> genericType) {
        super(em, genericType);
    }

    @Override
    public void create(PrivateClient client) {
        super.create(client);
    }
}
