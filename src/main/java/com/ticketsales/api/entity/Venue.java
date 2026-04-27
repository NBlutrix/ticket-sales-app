package com.ticketsales.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Integer totalCapacity;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Seat> seats;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Event> events;
}
