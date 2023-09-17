package com.leyou.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;
//Entité pour la recherche sur la page du portail
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    private Long id; // spuId

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all; // L'ensemble des informations à trouver, y compris le titre, le catalogue et la marque

    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;

    private Long brandId;
    private Long cid1;// ID de catalogue de niveau 1
    private Long cid2;// ID de catalogue de niveau 2
    private Long cid3;// ID de catalogue de niveau 3
    private Date createTime;
    private List<Long> price; //Liste des prix de tous les SKU sous le même SPU

    @Field(type = FieldType.Keyword, index = false)
    private String skus;// Liste <sku> de données JSON

    private Map<String, Object> specs;// Spécifications à trouver sur la page d'accueil

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCid1() {
        return cid1;
    }

    public void setCid1(Long cid1) {
        this.cid1 = cid1;
    }

    public Long getCid2() {
        return cid2;
    }

    public void setCid2(Long cid2) {
        this.cid2 = cid2;
    }

    public Long getCid3() {
        return cid3;
    }

    public void setCid3(Long cid3) {
        this.cid3 = cid3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Long> getPrice() {
        return price;
    }

    public void setPrice(List<Long> price) {
        this.price = price;
    }

    public String getSkus() {
        return skus;
    }

    public void setSkus(String skus) {
        this.skus = skus;
    }

    public Map<String, Object> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, Object> specs) {
        this.specs = specs;
    }
}