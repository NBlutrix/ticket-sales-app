package com.ticketsales.api.service;

import com.ticketsales.api.dto.request.BookingRequest;
import com.ticketsales.api.entity.Booking;
import com.ticketsales.api.entity.Seat;
import com.ticketsales.api.entity.User;
import com.ticketsales.api.repository.BookingRepository;
import com.ticketsales.api.repository.EventRepository;
import com.ticketsales.api.repository.SeatRepository;
import com.ticketsales.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public Booking createBooking(BookingRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
            throw new RuntimeException("Seat is not available");
        }

        var event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        seat.setStatus(Seat.SeatStatus.BOOKED);
        seatRepository.save(seat);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSeat(seat);
        booking.setEvent(event);
        booking.setBookingReference(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUserId(user.getId());
    }
}