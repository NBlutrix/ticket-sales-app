package com.ticketsales.api.controller;

import com.ticketsales.api.entity.Event;
import com.ticketsales.api.entity.Seat;
import com.ticketsales.api.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllActiveEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<Seat>> getSeatsByEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getSeatsByEvent(id));
    }
}