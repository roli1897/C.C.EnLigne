package com.leyou.item.repository;

import com.leyou.item.pojo.Spu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpuRepository extends JpaRepository<Spu, Long>, JpaSpecificationExecutor<Spu> {

}
