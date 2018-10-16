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
    private static final int TIME_INTERVAL_MILISECONDS = 2000;
    private static final int LATEST_ELEMENTS_NUMBER = 100;

    private Deque<Item> itemList;

    public ItemRepositoryImpl() {
        itemList = new LinkedList<>();
    }

    public Deque<Item> getAllItems() {
        return itemList;
    }

    public Deque<Item> getItems() {
        Deque<Item> resultList = new LinkedList<Item>();
        if (itemList.size() <= LATEST_ELEMENTS_NUMBER) {
            resultList.addAll(itemList);
        } else {
            int itemsLastTwoSeconds = 0;
            Timestamp currentTimestamp = new Timestamp((new Date().getTime()));

            for (Item item : itemList) {
                if (currentTimestamp.getTime() - item.getTimestamp().getTime() < TIME_INTERVAL_MILISECONDS) {
                    itemsLastTwoSeconds++;
                }
            }

            if (itemsLastTwoSeconds <= LATEST_ELEMENTS_NUMBER) {
                for (Item item : itemList) {
                    resultList.addLast(item);
                    if (resultList.size() == LATEST_ELEMENTS_NUMBER) {
                        break;
                    }
                }
            } else {
                int i = 0;
                for (Iterator<Item> iterator = itemList.iterator(); iterator.hasNext(); ) {
                    Item item = iterator.next();
                    if (currentTimestamp.getTime() - item.getTimestamp().getTime() < TIME_INTERVAL_MILISECONDS) {
                        resultList.addLast(item);
                    } else if (i > LATEST_ELEMENTS_NUMBER) {
                        // Remove elements that are not in the first 100 and have a timestamp older than 2 seconds.
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
