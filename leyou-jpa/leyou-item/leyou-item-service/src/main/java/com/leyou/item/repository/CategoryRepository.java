package com.leyou.item.repository;

import com.leyou.item.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Category> findByParentId(Long pid);
    @Query(value="delete from tb_category_brand where category_id = ?1 ", nativeQuery = true)
    @Modifying
    void deleteByCategoryIdInCategoryBrand(Long id);

    @Query(value="select * from tb_category where id in (select category_id from tb_category_brand where brand_id = ?1)", nativeQuery = true)
    List<Category> findByBrandId(Long bid);


}
