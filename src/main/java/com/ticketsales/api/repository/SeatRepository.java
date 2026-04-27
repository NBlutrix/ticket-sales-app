package com.ticketsales.api.repository;

import com.ticketsales.api.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByVenue(Long venueId);
    List<Seat> findByVenueAndStatus(Long venueId, Seat.SeatStatus status);
}
