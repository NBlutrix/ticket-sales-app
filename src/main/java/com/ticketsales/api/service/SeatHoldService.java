package com.ticketsales.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SeatHoldService {

    private final StringRedisTemplate redisTemplate;

    private static final long HOLD_DURATION_MINUTES = 10;
    private static final String KEY_PREFIX = "seat:hold:";

    public boolean holdSeat(Long seatId, String userEmail) {
        String key = KEY_PREFIX + seatId;
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, userEmail, Duration.ofMinutes(HOLD_DURATION_MINUTES));
        return Boolean.TRUE.equals(success);
    }

    public void releaseSeat(Long seatId) {
        redisTemplate.delete(KEY_PREFIX + seatId);
    }

    public boolean isSeatHeld(Long seatId) {
        return redisTemplate.hasKey(KEY_PREFIX + seatId);
    }

    public String getSeatHoldOwner(Long seatId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + seatId);
    }
}