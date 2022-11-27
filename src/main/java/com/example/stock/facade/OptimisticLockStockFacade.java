package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public int decrease(Long id, Long quantity) throws InterruptedException {
        int cnt = 1;
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                return cnt;
            } catch (Exception e) {
                Thread.sleep(500);
                cnt++;
            }
        }
    }
}
