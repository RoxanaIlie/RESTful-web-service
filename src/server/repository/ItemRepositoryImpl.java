package server.repository;


import org.springframework.stereotype.Component;
import server.exception.ItemAlreadyExists;
import server.exception.ItemNotFound;
import server.model.Item;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private Deque<Item> itemList;

    public ItemRepositoryImpl() {
        itemList = new LinkedList<>();
    }

    public Deque<Item> getAllItems() {
        return itemList;
    }

    public Deque<Item> getItems() {
        Deque<Item> resultList = new LinkedList<Item>();
        if (itemList.size() < 101) {
            resultList.addAll(itemList);
        } else {
            int itemsLastTwoSeconds = 0;
            Timestamp currentTimestamp = new Timestamp((new Date().getTime()));
            for (Item item : itemList) {
                if (currentTimestamp.getTime() - item.getTimestamp().getTime() < 2000) {
                    itemsLastTwoSeconds++;
                }
            }

            if (itemsLastTwoSeconds < 101) {
                int i = 0;
                for (Item item : itemList) {
                    resultList.addLast(item);
                    i++;
                    if (i == 100) {
                        break;
                    }
                }
            } else {
                int i = 0;
                for (Iterator<Item> iterator = itemList.iterator(); iterator.hasNext(); ) {
                    Item item = iterator.next();
                    if (currentTimestamp.getTime() - item.getTimestamp().getTime() < 2000) {
                        resultList.addLast(item);
                    } else if (i > 100) {
                        iterator.remove();
                    }
                    i++;
                }
            }
        }

        return resultList;
    }

    public Item getItem(int id) {
        for (Item item : itemList) {
            if (item.getId() == id) {
                return item;
            }
        }
        throw new ItemNotFound("ID: " + id);
    }


    public void deleteItem(int id) {
        for (Item item : itemList) {
            if (item.getId() == id) {
                itemList.remove(item);
                return;
            }
        }
        throw new ItemNotFound("ID: " + id);
    }

    public void addItem(Item item) {
        for (Item itm : itemList) {
            if (itm.getId() == item.getId()) {
                throw new ItemAlreadyExists("ID: " + itm.getId());
            }
        }
        itemList.addFirst(item);
    }
}
