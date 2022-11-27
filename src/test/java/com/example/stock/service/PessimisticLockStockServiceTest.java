package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PessimisticLockStockServiceTest {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    PessimisticLockStockService pessimisticLockStockService;

    @BeforeEach
    void setup() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    void clear() {
        stockRepository.deleteAllInBatch();
    }

    @Test
    void pessimisticLock_test() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    pessimisticLockStockService.decrease(1L, 1l);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(0, stockRepository.findById(1L).orElseThrow().getQuantity());
    }
}