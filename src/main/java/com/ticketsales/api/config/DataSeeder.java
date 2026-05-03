package com.ticketsales.api.config;

import com.ticketsales.api.entity.Event;
import com.ticketsales.api.entity.Seat;
import com.ticketsales.api.entity.Venue;
import com.ticketsales.api.repository.EventRepository;
import com.ticketsales.api.repository.SeatRepository;
import com.ticketsales.api.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    @Override
    public void run(String... args) {
        if (venueRepository.count() > 0) {
            return;
        }

        Venue venue = new Venue();
        venue.setName("Stark Arena");
        venue.setCity("Beograd");
        venue.setTotalCapacity(100);
        venueRepository.save(venue);

        String[] rows = {"A", "B", "C", "D", "E"};
        for (String row : rows) {
            for (int i = 1; i <= 20; i++) {
                Seat seat = new Seat();
                seat.setRowLabel(row);
                seat.setSeatNumber(i);
                seat.setVenue(venue);
                seatRepository.save(seat);
            }
        }

        Event event1 = new Event();
        event1.setName("Novogodišnji koncert");
        event1.setDescription("Spektakularni novogodišnji koncert u Stark Areni");
        event1.setEventDate(LocalDateTime.of(2026, 12, 31, 20, 0));
        event1.setPrice(2500.0);
        event1.setVenue(venue);
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setName("Rock Fest 2026");
        event2.setDescription("Najveći rock festival u regionu");
        event2.setEventDate(LocalDateTime.of(2026, 8, 15, 18, 0));
        event2.setPrice(1500.0);
        event2.setVenue(venue);
        eventRepository.save(event2);
    }
}