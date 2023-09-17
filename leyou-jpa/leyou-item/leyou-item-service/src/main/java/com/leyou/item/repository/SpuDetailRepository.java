package com.leyou.item.repository;

import com.leyou.item.pojo.SpuDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpuDetailRepository extends JpaSpecificationExecutor<SpuDetail>, JpaRepository<SpuDetail, Long> {
}
