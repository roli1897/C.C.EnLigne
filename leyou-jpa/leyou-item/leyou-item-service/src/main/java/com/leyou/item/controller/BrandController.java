package com.leyou.item.controller;


import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;


    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc" ,required = false)Boolean desc
    ){
        PageResult<Brand> result =this.brandService.queryBrandsByPage(key, page, rows, sortBy, desc);
        if (CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);

    }

    @PostMapping
    public ResponseEntity<Void> saveBrand (Brand brand, @RequestParam("cids") List<Long> cids) {
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * Actualiser le catalogue
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand (Brand brand, @RequestParam("cids")List<Long> cids) {
        this.brandService.updateBrand(brand, cids);
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprimer le brand sur le tableau de tb_brand
     * @param bid
     * @return
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand (@PathVariable("bid") String bid) {
        String separator = "-";
        if (bid.contains(separator)) {
            String[] ids = bid.split(separator);
            for (String id : ids) {
                this.brandService.deleteBrand(Long.parseLong(id));
            }
        } else {
            this.brandService.deleteBrand(Long.parseLong(bid));
        }
        return ResponseEntity.noContent().build();
    }


    /**
     * Avec l'ID du catalogue, obtenez la liste des marques
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid") Long cid) {
        List<Brand> brands = this.brandService.queryBrandsByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
        Brand brand = this.brandService.queryBrandById(id);
        if (brand == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }


}
