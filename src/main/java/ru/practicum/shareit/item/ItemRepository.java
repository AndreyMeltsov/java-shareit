package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    void updateItem(Item item);

    Item findItemById(Integer id);

    List<Item> findItemsByUserId(Long userId);

    List<Item> getItemsByQuery(String query);
}
