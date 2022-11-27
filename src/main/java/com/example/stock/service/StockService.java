package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Transactional
//    public synchronized void decrease(Long id, long quantity) {
    public void decrease(Long id, long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decreaseQuantity(quantity);
        stockRepository.save(stock);
    }
}
