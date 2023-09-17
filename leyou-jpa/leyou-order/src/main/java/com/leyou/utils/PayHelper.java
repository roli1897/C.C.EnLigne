package com.leyou.utils;

import com.github.wxpay.sdk.WXPay;
import com.leyou.config.PayConfig;
import com.leyou.order.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PayHelper {

    private WXPay wxPay;

    private static final Logger logger = LoggerFactory.getLogger(PayHelper.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    public PayHelper(PayConfig payConfig) {

        wxPay = new WXPay(payConfig);
        // test
        // wxPay = new WXPay(payConfig, WXPayConstants.SignType.MD5, true);
    }

    public String createPayUrl(Long orderId) {
        String key = "ly.pay.url." + orderId;
        try {
            String url = this.redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(url)) {
                return url;
            }
        } catch (Exception e) {
            logger.error("查询缓存付款链接异常,订单编号：{}", orderId, e);
        }

        try {
            Map<String, String> data = new HashMap<>();
            // description du produit
            data.put("body", "乐优商城测试");
            // numéro du command
            data.put("out_trade_no", orderId.toString());
            //monnaie
            data.put("fee_type", "CNY");
            //montant, en unité de centimes
            data.put("total_fee", "1");
            //Adresse IP du terminal d'appel de paiement WeChat (IP de la boutique Leyou)
            data.put("spbill_create_ip", "127.0.0.1");
            //adresse de rappel, interface après le paiement réussi
            data.put("notify_url", "http://test.leyou.com/wxpay/notify");
            // le type de transaction est le paiement par code QR
            data.put("trade_type", "NATIVE");
            //ID du produit, en utilisant des données fictives
            data.put("product_id", "1234567");

            Map<String, String> result = this.wxPay.unifiedOrder(data);

            if ("SUCCESS".equals(result.get("return_code"))) {
                String url = result.get("code_url");
                // 将付款地址缓存，时间为10分钟
                try {
                    this.redisTemplate.opsForValue().set(key, url, 10, TimeUnit.MINUTES);
                } catch (Exception e) {
                    logger.error("缓存付款链接异常,订单编号：{}", orderId, e);
                }
                return url;
            } else {
                logger.error("创建预交易订单失败，错误信息：{}", result.get("return_msg"));
                return null;
            }
        } catch (Exception e) {
            logger.error("创建预交易订单异常", e);
            return null;
        }
    }

    /**
     * vérifier l'état de la commande
     *
     * @param orderId
     * @return
     */
   public PayState queryOrder(Long orderId) {
        Map<String, String> data = new HashMap<>();
        // numéro du command
        data.put("out_trade_no", orderId.toString());
        try {
            Map<String, String> result = this.wxPay.orderQuery(data);
            if (result == null) {
                // Aucun résultat trouvé, considéré comme non payé
                return PayState.NOT_PAY;
            }
            String state = result.get("trade_state");
            if ("SUCCESS".equals(state)) {
                // si succès, alors considéré comme un paiement réussi

                // Modifier l'état de la commande
                this.orderService.updateStatus(orderId, 2);
                return PayState.SUCCESS;
            } else if (StringUtils.equals("USERPAYING", state) || StringUtils.equals("NOTPAY", state)) {
                // Non payé, que ce soit en cours de paiement ou non payé
                return PayState.NOT_PAY;
            } else {
                // Tous les autres états sont considérés comme un échec de paiement
                return PayState.FAIL;
            }
        } catch (Exception e) {
            logger.error("查询订单状态异常", e);
            return PayState.NOT_PAY;
        }
    }
}