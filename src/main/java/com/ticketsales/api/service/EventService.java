package com.ticketsales.api.service;

import com.ticketsales.api.entity.Event;
import com.ticketsales.api.entity.Seat;
import com.ticketsales.api.repository.EventRepository;
import com.ticketsales.api.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public List<Event> getAllActiveEvents() {
        return eventRepository.findByStatus(Event.EventStatus.ACTIVE);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));
    }

    public List<Seat> getSeatsByEvent(Long eventId) {
        Event event = getEventById(eventId);
        return seatRepository.findByVenueId(event.getVenue().getId());
    }
}