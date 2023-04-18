package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Item createItem(Long userId, ItemDto itemDto) {
        userService.findUserById(userId);
        Item item = ItemMapper.toItem(itemDto, userId, null);
        item = itemRepository.createItem(item);
        log.info("Item is added: {}", item);
        return item;
    }

    @Override
    public ItemDto updateItem(Long userId, Integer id, ItemDto itemDto) {
        Item item = itemRepository.findItemById(id);
        if (!item.getOwner().equals(userId)) {
            throw new NotFoundException("Attempt to update item by user who isn't its owner");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.updateItem(item);
        log.info("Item was updated in DB. New item is: {}", item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto findItemById(Long userId, Integer id) {
        userService.findUserById(userId);
        Item item = itemRepository.findItemById(id);
        log.info("Item was found in DB: {}", item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findItemsByUserId(Long userId) {
        userService.findUserById(userId);
        List<ItemDto> userItems = itemRepository.findItemsByUserId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("User with id {} has {} items", userId, userItems.size());
        return userItems;
    }

    @Override
    public List<ItemDto> getItemsByQuery(Long userId, String query) {
        userService.findUserById(userId);
        List<ItemDto> userItems = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            log.info("Query is empty or null");
            return userItems;
        }
        userItems = itemRepository.getItemsByQuery(query).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("{} items were found for \"{}\" query", userItems.size(), query);
        return userItems;
    }
}
