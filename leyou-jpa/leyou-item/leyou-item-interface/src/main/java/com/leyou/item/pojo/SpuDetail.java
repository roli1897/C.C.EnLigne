package com.leyou.item.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;// ID correspondant au SPU
    private String description;// Description du produit
    private String specialSpec;// Nom des spécifications spéciales du produit et modèle des valeurs possibles
    private String genericSpec;// Attributs de spécification globale du produit
    private String packingList;// Attributs de spécification globale du produit
    private String afterService;// le serivce après ventre
    // getter和setter

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(String specialSpec) {
        this.specialSpec = specialSpec;
    }

    public String getGenericSpec() {
        return genericSpec;
    }

    public void setGenericSpec(String genericSpec) {
        this.genericSpec = genericSpec;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }
}