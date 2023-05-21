package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntegrityTest {
    private final EntityManager em;
    private final BookingService service;

    User user;
    User owner;
    Item item;

    @BeforeEach
    void setUserAndItemAndOwner() {
        User userEntity = new User(null, "Ivan", "ivan@email");
        em.persist(userEntity);
        em.flush();

        TypedQuery<User> queryForUser = em.createQuery("Select u from User u where u.email = :email", User.class);
        user = queryForUser.setParameter("email", userEntity.getEmail())
                .getSingleResult();

        User ownerEntity = new User(null, "Owner", "owner@email");
        em.persist(ownerEntity);
        em.flush();

        TypedQuery<User> queryForOwner = em.createQuery("Select u from User u where u.email = :email", User.class);
        owner = queryForOwner.setParameter("email", ownerEntity.getEmail())
                .getSingleResult();

        Item itemEntity = new Item(null, "name",
                "description", true, owner.getId(), null);
        em.persist(itemEntity);
        em.flush();

        TypedQuery<Item> queryForItem = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        item = queryForItem.setParameter("name", itemEntity.getName())
                .getSingleResult();
    }

    @Test
    void createBooking() {
        BookingDto bookingDto = new BookingDto(
                null,
                item.getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                user.getId()
        );

        service.createBooking(user.getId(), bookingDto);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b join b.item i where i.id = :itemId", Booking.class);
        Booking savedBooking = query.setParameter("itemId", bookingDto.getItemId())
                .getSingleResult();

        assertThat(savedBooking.getId(), notNullValue());
        assertThat(savedBooking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(savedBooking.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(savedBooking.getItem(), equalTo(item));
        assertThat(savedBooking.getBooker(), equalTo(user));
        assertThat(savedBooking.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void approveBooking() {
        Booking booking = new Booking(
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.WAITING
        );
        em.persist(booking);
        em.flush();

        TypedQuery<Long> queryForId = em.createQuery(
                "Select b.id from Booking b join b.item i where i.id = :itemId", Long.class);
        Long entityId = queryForId.setParameter("itemId", item.getId())
                .getSingleResult();

        service.approveBooking(owner.getId(), entityId, true);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :bookingId", Booking.class);
        Booking updatedBooking = query.setParameter("bookingId", entityId)
                .getSingleResult();

        assertThat(updatedBooking.getId(), notNullValue());
        assertThat(updatedBooking.getStart(), equalTo(booking.getStart()));
        assertThat(updatedBooking.getEnd(), equalTo(booking.getEnd()));
        assertThat(updatedBooking.getItem(), equalTo(item));
        assertThat(updatedBooking.getBooker(), equalTo(user));
        assertThat(updatedBooking.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void findBookingById() {
        Booking booking = new Booking(
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.WAITING
        );
        em.persist(booking);
        em.flush();

        TypedQuery<Long> queryForId = em.createQuery(
                "Select b.id from Booking b join b.item i where i.id = :itemId", Long.class);
        Long entityId = queryForId.setParameter("itemId", item.getId())
                .getSingleResult();

        Booking targetBooking = service.findBookingById(user.getId(), entityId);

        assertThat(targetBooking.getId(), notNullValue());
        assertThat(targetBooking.getStart(), equalTo(booking.getStart()));
        assertThat(targetBooking.getEnd(), equalTo(booking.getEnd()));
        assertThat(targetBooking.getItem(), equalTo(item));
        assertThat(targetBooking.getBooker(), equalTo(user));
        assertThat(targetBooking.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void findBookingsByState() {
        List<Booking> sourceBookings = List.of(
                new Booking(null,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        item,
                        user,
                        BookingStatus.CANCELED
                ),
                new Booking(null,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        item,
                        user,
                        BookingStatus.APPROVED
                ),
                new Booking(null,
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().plusDays(3),
                        item,
                        user,
                        BookingStatus.WAITING
                )
        );

        for (Booking sourceBooking : sourceBookings) {
            em.persist(sourceBooking);
        }
        em.flush();

        List<Booking> targetBookings = service.findBookingsByState(user.getId(), "all", 0, 10);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
        for (Booking booking : sourceBookings) {
            assertThat(targetBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", equalTo(booking.getItem())),
                    hasProperty("booker", equalTo(booking.getBooker())),
                    hasProperty("status", equalTo(booking.getStatus()))
            )));
        }
    }

    @Test
    void findBookingByStateForOwner() {
        List<Booking> sourceBookings = List.of(
                new Booking(null,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        item,
                        user,
                        BookingStatus.CANCELED
                ),
                new Booking(null,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        item,
                        user,
                        BookingStatus.APPROVED
                ),
                new Booking(null,
                        LocalDateTime.now().plusDays(2),
                        LocalDateTime.now().plusDays(3),
                        item,
                        user,
                        BookingStatus.WAITING
                )
        );

        for (Booking sourceBooking : sourceBookings) {
            em.persist(sourceBooking);
        }
        em.flush();

        List<Booking> targetBookings = service.findBookingByStateForOwner(owner.getId(), "all", 0, 10);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
        for (Booking booking : sourceBookings) {
            assertThat(targetBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", equalTo(booking.getItem())),
                    hasProperty("booker", equalTo(booking.getBooker())),
                    hasProperty("status", equalTo(booking.getStatus()))
            )));
        }
    }
}