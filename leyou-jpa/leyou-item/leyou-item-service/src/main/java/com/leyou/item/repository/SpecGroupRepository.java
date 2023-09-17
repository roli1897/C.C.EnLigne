package com.leyou.item.repository;

import com.leyou.item.pojo.SpecGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;

public interface SpecGroupRepository extends JpaRepository<SpecGroup, Long>, JpaSpecificationExecutor<SpecGroup> {

    List<SpecGroup> findByCid(Long cid);
}
