package com.leyou.cart.service;


import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "user:cart:";

    public void addCart(Cart cart) {

        //obtenir les infos d'utilisateur
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //Rechercher les enregistrements du panier
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();

        //Déterminer si le produit actuel se trouve dans le panier
        if (hashOperations.hasKey(key)) {
            //si dans le panier, modifier les quantité
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        } else {
            //si pas das le panier, modifier le panier
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setPrice(sku.getPrice());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setTitle(sku.getTitle());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
        }
        hashOperations.put(key, JsonUtils.serialize(cart)); //Enregistrez toutes les données sous forme de chaînes dans Redis

    }

    public List<Cart> queryCarts() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //  Déterminer si l'utilisateur a des enregistrements dans le panier
        String key = KEY_PREFIX + userInfo.getId();
        if ( ! this.redisTemplate.hasKey(key) ) {
            return null;
        }
        //Obtenir les enregistrements du panier de l'utilisateur
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);
        //Obtenir la collection de toutes les valeurs 'cart' dans la carte du panier
        List<Object> cartsJson = hashOperations.values();
        //Si la collection du panier est vide, retourner directement null
        if(CollectionUtils.isEmpty(cartsJson)) {
            return null;
        }
        //Pour convertir une liste List<Object> en une liste List<Cart>
        return cartsJson.stream().map(cartJson -> JsonUtils.parse(cartJson.toString(), Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(Cart cart) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //Pour déterminer si un utilisateur a des enregistrements dans son panier,
        String key = KEY_PREFIX + userInfo.getId();
        if ( ! this.redisTemplate.hasKey(key) ) {
            return;
        }
        Integer num = cart.getNum();
        //Pour obtenir les enregistrements du panier d'un utilisateur,
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);
        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        cart = JsonUtils.parse(cartJson, Cart.class);
        cart.setNum(num);
        hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    public void deleteCart(String skuId) {
        //Obtenir l'utilisateur connecté
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFIX + userInfo.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }
}