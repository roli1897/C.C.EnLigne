package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadData(Long spuId) {
        Map<String, Object> model = new HashMap<>();

        //rechercher le spu par spuid
        Spu spu = this.goodsClient.querySpuById(spuId);
        //rechercher spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        //rechercher le catégories：Map<String, Object>
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNameByIds(cids);
        //initialiser une category :list map
        List<Map<String, Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }
        //rechercher une brand
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);
        //Rechercher un groupe de paramètres de spécifications
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());
        //Rechercher des paramètres de spécifications spécifiques
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        //Initialiser une "map" de paramètres de spécifications spécifiques
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });

        model.put("spu", spu);
        model.put("spuDetail", spuDetail);
        model.put("categories", categories);
        model.put("brand", brand);
        model.put("skus", skus);
        model.put("groups", groups);
        model.put("paramMap", paramMap);
        return model;
    }
}