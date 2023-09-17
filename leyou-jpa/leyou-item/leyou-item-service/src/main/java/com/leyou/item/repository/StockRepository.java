package com.leyou.item.repository;

import com.leyou.item.pojo.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockRepository extends JpaSpecificationExecutor<Stock>, JpaRepository<Stock, Long> {
}
