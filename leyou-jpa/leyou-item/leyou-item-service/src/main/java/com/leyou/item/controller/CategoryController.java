package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesFindByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        if (pid == null || pid < 0) {

            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = this.categoryService.queryCategoriesByPid(pid);
        if (CollectionUtils.isEmpty(categories)) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categories);

    }
    @PostMapping
    public ResponseEntity<Void> saveCategory(Category category) {
        this.categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Actualiser le catalogue
     * @param category
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCategory(Category category) {
        this.categoryService.updateCategory(category);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Supprimer le catalogue
     * @param id
     * @return
     */
    @DeleteMapping("cid/{cid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("cid") Long id) {
        this.categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Lors de la mise à jour de la marque, affiche la liste des catalogues
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>>  queryCategoriesByBrandId(@PathVariable("bid") Long bid) {
        List<Category> categories = this.categoryService.queryCategoriesByBrandId(bid);
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    /**
     * Liste d'identifiants de catalogue -> Liste de noms de catalogue
     * @param ids
     * @return
     */
    @GetMapping
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids) {
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

    /**
     * cid3 -> Liste de catalogues pour cid1,2,3
     * @param cid3
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id")Long cid3) {
        List<Category> categories = this.categoryService.queryAllByCid3(cid3);
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }
}
