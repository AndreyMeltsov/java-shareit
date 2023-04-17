package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Booking {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;

    private BookingStatus status;  // WAITING, APPROVED, REJECTED or CANCELED
}
