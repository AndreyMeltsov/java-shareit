package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Integer id,
                              @RequestBody Map<String, Object> fields) {
        return itemService.updateItem(userId, id, fields);
    }

    @GetMapping("/{id}")
    public ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @PathVariable Integer id) {
        return itemService.findItemById(userId, id);
    }

    @GetMapping
    public List<ItemDto> findItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(value = "text", required = false) String query) {
        return itemService.getItemsByQuery(userId, query);
    }

}
