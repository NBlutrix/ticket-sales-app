package com.ticketsales.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ticketsales.api.dto.request.BookingRequest;
import com.ticketsales.api.service.BookingService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class OptimisticLockTest {

    @Autowired
    private BookingService bookingService;

    @Test
    void testConcurrentBooking() throws InterruptedException {
        int numberOfThreads = 2;
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        BookingRequest request = new BookingRequest();
        request.setEventId(1L);
        request.setSeatId(3L);

        for (int i = 0; i < numberOfThreads; i++) {
            final String email = i == 0 ? "user1@test.com" : "user2@test.com";
            executor.submit(() -> {
                try {
                    latch.await();
                    bookingService.createBooking(request, email);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("Expected conflict: " + e.getClass().getSimpleName());
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        Thread.sleep(3000);

        System.out.println("Success: " + successCount.get());
        System.out.println("Failed: " + failCount.get());

        assert successCount.get() == 1 : "Only one booking should succeed";
        assert failCount.get() == 1 : "One booking should fail";
    }
}