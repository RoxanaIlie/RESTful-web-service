package client.rest;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import server.model.Item;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Deque;

public class RestClient {

    private static final String REST_URI = "http://localhost:8080/roxana_items_sap_war_exploded/items";
    private ResteasyClient client = new ResteasyClientBuilder().build();

    public Response createJsonItem(Item item) {
        return client.target(REST_URI).request(MediaType.APPLICATION_JSON).post(Entity.entity(item, MediaType.APPLICATION_JSON));
    }

    public Deque<Item> getJsonItems() {
        return client.target(REST_URI).request(MediaType.APPLICATION_JSON).get(new GenericType<Deque<Item>>() {
        });
    }

}