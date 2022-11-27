package com.example.stock.facade;

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

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class LettuceLockStockFacadeTest {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    LettuceLockStockFacade lettuceLockStockFacade;

    @BeforeEach
    void setup() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    void clear() {
        stockRepository.deleteAllInBatch();
    }

    @Test
    void namedLock_test() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    lettuceLockStockFacade.decrease(1L, 1l);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(0, stockRepository.findById(1L).orElseThrow().getQuantity());
    }

}