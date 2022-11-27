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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OptimisticLockStockFacadeTest {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    OptimisticLockStockFacade optimisticLockStockFacade;

    @BeforeEach
    void setup() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    void clear() {
        stockRepository.deleteAllInBatch();
    }

    @Test
    void optimisticLock_test() throws InterruptedException {
        int threadCount = 100;
        AtomicInteger total = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    int decrease = optimisticLockStockFacade.decrease(1L, 1l);
                    total.addAndGet(decrease);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();

                }
            });
        }

        latch.await();

        System.out.println(total);

        assertEquals(0, stockRepository.findById(1L).orElseThrow().getQuantity());
    }
}