package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    Booking createBooking(Long userId, BookingDto bookingDto);

    Booking approveBooking(Long userId, Long bookingId, Boolean approved);

    Booking findBookingById(Long userId, Long bookingId);

    List<Booking> findBookingsByState(Long userId, String state, Integer from, Integer size);

    List<Booking> findBookingByStateForOwner(Long userId, String state, Integer from, Integer size);
}
