package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * spu ->  Goods (search)
     *
     * @param spu
     * @return
     * @throws IOException
     */
    public Goods buildGoods(Spu spu) throws IOException {

        // Créer un objet de type "goods"
        Goods goods = new Goods();

        // rechercher une marque
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        // Rechercher le nom de la catégorie
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        // Rechercher tous les SKU sous un SPU
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        // Parcourir les SKUs pour obtenir une collection de prix
        skus.forEach(sku ->{
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isNotBlank(sku.getImages()) ? StringUtils.split(sku.getImages(), ",")[0] : "");
            skuMapList.add(skuMap);
        });

        // Obtenir tous les paramètres de spécifications de recherche
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), null, true);
        // Rechercher SPU (Single Product Unit) Detail pour obtenir les valeurs des paramètres de spécification
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        // Obtenir les spécifications de paramètres génériques
        Map<Long, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        // Obtenir les spécifications de paramètres spécifiques
        Map<Long, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>() {
        });
        // Définir une carte pour recevoir {nom du paramètre de spécification, valeur du paramètre de spécification}
        Map<String, Object> paramMap = new HashMap<>();
        params.forEach(param -> {
            // Déterminer si c'est un paramètre de spécification générique
            if (param.getGeneric()) {
                // Obtenir les valeurs des paramètres de spécification génériques
                String value = genericSpecMap.get(param.getId()).toString();
                // Déterminer si c'est un type de données numériques
                if (param.getNumeric()){
                    // Si c'est un nombre, déterminer dans quelle plage il se situe
                    value = chooseSegment(value, param);
                }
                // Placer le nom du paramètre et sa valeur dans l'ensemble de résultats
                paramMap.put(param.getName(), value);
            } else {
                paramMap.put(param.getName(), specialSpecMap.get(param.getId()));
            }
        });

        //Configurer les paramètres
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle() + brand.getName() + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        goods.setSpecs(paramMap);

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // Enregistrer l'intervalle de valeurs
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // Obtenir la plage de valeurs numériques
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // Déterminer si cela se trouve dans la plage
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


    public SearchResult search(SearchRequest request) {
        //Si aucune mots-clés de recherche n'est fournie, retourner null pour ne pas afficher tous les produits
        if (StringUtils.isBlank(request.getKey())) {
            return null;
        }
        //Créateur de recherche native
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //Conditions de recherche
//        QueryBuilder basicQuery = QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND);
        BoolQueryBuilder basicQuery = buildBoolQueryBuilder(request);
        queryBuilder.withQuery(basicQuery);
        //pagination
        queryBuilder.withPageable(PageRequest.of(request.getPage() - 1, request.getSize()));
        //sort
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if (StringUtils.isNotBlank(sortBy)) {
            //Si ce n'est pas vide, alors trier
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
        }
        //_source
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));

        //Aggrégation pour les catalogues et les marques
        String categoryAggName = "categories";
        String brandAggName = "brands";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //Effectuer la recherche
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        //aggResult
        List<Map<String, Object>> categories = getCategoryAggResult(goodsPage.getAggregation(categoryAggName));
        List<Brand> brands = getBrandAggResult(goodsPage.getAggregation(brandAggName));

        //Si c'est un seul catalogue, afficher les spécifications
        List<Map<String, Object>> specs = null;
        if (!CollectionUtils.isEmpty(categories) && categories.size() == 1) {
            //aggregation para parámetros
            specs = getParamAggResult((Long) categories.get(0).get("id"), basicQuery);
        }
        return new SearchResult(goodsPage.getTotalElements(), goodsPage.getTotalPages(), goodsPage.getContent(), categories, brands, specs);
    }

    /**
     * BoolQueryBuilder
     *
     * @param request
     * @return
     */
    private BoolQueryBuilder buildBoolQueryBuilder(SearchRequest request) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //Ajouter des conditions de requête de base à la requête booléenne
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        //Ajouter des conditions de filtrage
        //Obtenir les informations de filtrage choisies par l'utilisateur
        Map<String, Object> filter = request.getFilter();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.equals("品牌", key)) {
                key = "brandId";
            } else if (StringUtils.equals("分类", key)) {
                key = "cid3";
            } else {
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }
        return boolQueryBuilder;
    }

    /**
     * Aggréger les paramètres de spécification en fonction des critères de recherche
     *
     * @param cid
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> getParamAggResult(Long cid, QueryBuilder basicQuery) {
        //Construction d'un objet de requête personnalisée
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //Ajouter des critères de recherche de base
        queryBuilder.withQuery(basicQuery);
        //Requête pour agréger les paramètres de spécification
        List<SpecParam> params = this.specificationClient.queryParams(null, cid, null, true);
        //Ajouter un filtrage des résultats
        params.forEach(param -> {
            // parce que les paramètres de spécification ne sont pas segmentés lors de leur enregistrement,
            // c'est pourquoi leur nom est automatiquement suivi d'un suffixe '.keyword'.
            queryBuilder.addAggregation(AggregationBuilders.terms(param.getName()).field("specs." + param.getName() + ".keyword"));
        });
        //Exécuter une requête d'agrégation
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        //Analyser les résultats de l'agrégation
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
        List<Map<String, Object>> specs = new ArrayList<>();
        //Analyser les résultats de l'agrégation, clé-nom de l'agrégation (nom du paramètre de spécification), valeur-objet d'agrégation
        Map<String, Aggregation> aggregationMap = goodsPage.getAggregations().asMap();
        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            //Initialiser une "map" {k: nom du paramètre de spécification, options: valeurs des paramètres de spécification agrégés}
            Map<String, Object> map = new HashMap<>();
            map.put("k", entry.getKey());
            //Initialiser une collection d'options, collecter les clés des seaux
            List<String> options = new ArrayList<>();
            //Obtenir l'agrégation
            StringTerms terms = (StringTerms) entry.getValue();
            //Obtenir l'agrégation par seau
            terms.getBuckets().forEach(bucket -> {
                options.add(bucket.getKeyAsString());
            });
            map.put("options", options);
            specs.add(map);
        }
        return specs;
    }

    /**
     * Analyser les résultats d'agrégation des marques
     *
     * @param aggregation
     * @return
     */
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;

        //Obtenir les seaux de l'agrégation
        return terms.getBuckets().stream().map(bucket -> {
            return this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());

        /*List<Brand> brands = new ArrayList<>();
        //Obtenir les seaux de l'agrégation
        terms.getBuckets().forEach(bucket -> {
            Brand brand = this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
            brands.add(brand);
        });
        return brands;*/
    }

    /**
     * Analyser les résultats d'agrégation des catégories
     *
     * @param aggregation
     * @return
     */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;

        //Obtenir la collection de seaux, la convertir en une collection de Listes de Maps
        return terms.getBuckets().stream().map(bucket -> {
            Map<String, Object> map = new HashMap<>();
            //Obtenir l'ID de catégorie (clé) des seaux
            Long id = bucket.getKeyAsNumber().longValue();
            //Rechercher le nom de la catégorie en fonction de l'ID de catégorie
            List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(id));
            map.put("id", id);
            map.put("name", names.get(0));
            return map;
        }).collect(Collectors.toList());

    }


    public void save(Long id) throws IOException {
        Spu spu = this.goodsClient.querySpuById(id);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }

    public void delete(Long id) {
        this.goodsRepository.deleteById(id);
    }
}