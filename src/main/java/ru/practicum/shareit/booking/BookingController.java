package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId,
                                 @RequestParam(value = "approved") Boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking findBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> findBookingsByState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(value = "state", defaultValue = "ALL",
                                                     required = false) String state,
                                             @RequestParam(value = "from", defaultValue = "0",
                                                     required = false) @Min(0) Integer from,
                                             @RequestParam(value = "size", defaultValue = "20",
                                                     required = false) @Min(1) Integer size) {
        return bookingService.findBookingsByState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> findBookingsByStateForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(value = "state", defaultValue = "ALL",
                                                             required = false) String state,
                                                     @RequestParam(value = "from", defaultValue = "0",
                                                             required = false) @Min(0) Integer from,
                                                     @RequestParam(value = "size", defaultValue = "20",
                                                             required = false) @Min(1) Integer size) {
        return bookingService.findBookingByStateForOwner(userId, state, from, size);
    }
}
