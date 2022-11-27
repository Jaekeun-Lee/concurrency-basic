package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setup() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    void clear() {
        stockRepository.deleteAllInBatch();
    }

    @Test
    void 재고_감소_테스트() {
        stockService.decrease(1L, 1);

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(99, stock.getQuantity());
    }

    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1l);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 0 일수도 아닐 수도 있음
        assertNotEquals(0, stockRepository.findById(1L).orElseThrow().getQuantity());
    }


}