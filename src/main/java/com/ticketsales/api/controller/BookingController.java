package com.ticketsales.api.controller;

import com.ticketsales.api.dto.request.BookingRequest;
import com.ticketsales.api.entity.Booking;
import com.ticketsales.api.service.BookingService;
import com.ticketsales.api.service.SeatHoldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final SeatHoldService seatHoldService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.createBooking(request, userDetails.getUsername()));
    }

    @PostMapping("/hold")
    public ResponseEntity<Map<String, Object>> holdSeat(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean success = seatHoldService.holdSeat(request.getSeatId(), userDetails.getUsername());
        if (success) {
            messagingTemplate.convertAndSend(
                    "/topic/events/" + request.getEventId(),
                    (Object) Map.of("seatId", request.getSeatId(), "status", "HELD")
            );
            return ResponseEntity.ok(Map.of(
                    "message", "Seat held for 10 minutes",
                    "seatId", request.getSeatId()
            ));
        } else {
            return ResponseEntity.status(409).body(Map.of(
                    "error", "Seat is already held by another user"
            ));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.getUserBookings(userDetails.getUsername()));
    }
}