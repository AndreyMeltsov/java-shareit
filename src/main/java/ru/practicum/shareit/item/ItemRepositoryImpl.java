package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private int id;

    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        items.add(item);
        return item;
    }

    @Override
    public void updateItem(Item item) {
        Item oldItem = items.stream()
                .filter(i -> Objects.equals(i.getId(), item.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item with such id wasn't found"));
        items.set(items.indexOf(oldItem), item);
    }

    @Override
    public Item findItemById(Integer id) {
        return items.stream()
                .filter(i -> Objects.equals(i.getId(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item with such id wasn't found"));
    }

    @Override
    public List<Item> findItemsByUserId(Long userId) {
        return items.stream()
                .filter(i -> Objects.equals(i.getOwner(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsByQuery(String query) {
        Stream<Item> queryForNameField = items.stream()
                .filter(i -> i.getName()
                        .toLowerCase()
                        .contains(query.toLowerCase().trim()))
                .filter(i -> i.getAvailable().equals(true));
        Stream<Item> queryForDescriptionField = items.stream()
                .filter(i -> i.getDescription()
                        .toLowerCase()
                        .contains(query.toLowerCase().trim()))
                .filter(i -> i.getAvailable().equals(true));
        return Stream.concat(queryForNameField, queryForDescriptionField).distinct()
                .collect(Collectors.toList());
    }
}
