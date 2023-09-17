package com.leyou.item.repository;

import com.leyou.item.pojo.SpecParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpecParamRepository extends JpaRepository<SpecParam, Long>, JpaSpecificationExecutor<SpecParam> {

    List<SpecParam> findByGroupId(Long gid);
}
