package com.leyou.item.pojo;

import javax.persistence.*;
import java.util.Date;

@Entity //Standard Product Unit
@Table(name = "tb_spu")
public class Spu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long brandId;
    private Long cid1;// Catégorie de niveau 1
    private Long cid2;// Catégorie de niveau 2
    private Long cid3;// Catégorie de niveau 3
    private String title;// titre
    private String subTitle;// subtitre
    private Boolean saleable;// Est-ce disponible à la vente
    private Boolean valid;// Est-ce valide, utilisé pour la suppression logique
    private Date createTime;// le temps de créer
    private Date lastUpdateTime;// Date de dernière modification

    @Override
    public String toString() {
        return "Spu{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", cid1=" + cid1 +
                ", cid2=" + cid2 +
                ", cid3=" + cid3 +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", saleable=" + saleable +
                ", valid=" + valid +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Boolean getSaleable() {
        return saleable;
    }

    public void setSaleable(Boolean saleable) {
        this.saleable = saleable;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}