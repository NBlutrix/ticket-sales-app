package com.ticketsales.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rowLabel;

    @Column(nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    public enum SeatStatus {
        AVAILABLE, HELD, BOOKED
    }
}