package server.rest;

import server.model.Item;
import server.repository.ItemRepository;
import server.repository.ItemRepositoryImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Deque;

@Path("/items")
public class ItemResource {
    private static ItemRepository itemRepository = new ItemRepositoryImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Deque<Item> getItems() {
        return itemRepository.getItems();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postItem(Item item, @Context UriInfo uriInfo) {
        itemRepository.addItem(new Item(item.getId(), item.getTimestamp()));
        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }
}