package com.example.stock.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@NoArgsConstructor
@Slf4j
@Getter
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private long productId;

    @Column
    private long quantity;

    @Version
    private Long version;

    public Stock(Long id, long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public void decreaseQuantity(long quantity) {
        if (this.quantity - quantity < 0) {
            throw new RuntimeException("재고 없음.");
        }
        log.info("version = {} / quantity = {} / this.quantity = {}", this.version, quantity, this.quantity);
        this.quantity -= quantity;
    }
}
