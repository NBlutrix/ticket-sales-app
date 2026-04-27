package com.ticketsales.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {

    @NotNull
    private Long eventId;

    @NotNull
    private Long seatId;
}