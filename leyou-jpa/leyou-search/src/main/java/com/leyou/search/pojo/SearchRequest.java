package com.leyou.search.pojo;

import java.util.Map;

public class SearchRequest {
    private String key;//Mots-clés de recherche

    private Integer page;// page actuelle

    private String sortBy;

    private Boolean descending;

    private Map<String, Object> filter;

    private static final Integer DEFAULT_SIZE = 20;// taille fixe
    private static final Integer DEFAULT_PAGE = 1;

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // Vérifier que ce ne soit pas inférieur à 1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }
}