package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Integer id, ItemDto itemDto);

    ItemDto findItemById(Long userId, Integer id);

    List<ItemDto> findItemsByUserId(Long userId);

    List<ItemDto> getItemsByQuery(Long userId, String query);
}
