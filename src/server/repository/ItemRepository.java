package server.repository;

import server.model.Item;

import java.util.Deque;

public interface ItemRepository {

    public Deque<Item> getAllItems();

    public Deque<Item> getItems();

    public Item getItem(int id);

    public void deleteItem(int id);

    public void addItem(Item item);
}
