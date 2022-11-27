package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NamedLockStockFacade {

    private final LockRepository lockRepository;
    private final StockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(generateKey(id));
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(generateKey(id));
        }
    }

    private String generateKey(Long key) {
        return String.valueOf(key);
    }
}
