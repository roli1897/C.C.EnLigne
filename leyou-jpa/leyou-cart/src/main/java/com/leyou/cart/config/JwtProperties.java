package com.leyou.cart.config;

import com.leyou.common.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {

    private String pubKeyPath;// 公钥

    private String cookieName;

    private PublicKey publicKey; // 公钥

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * @PostConstruct : exécute cette méthode après l'exécution du constructeur
     */
    @PostConstruct
    public void init(){
        try {
            // Obtenir la clé publique et la clé privée
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            } catch (Exception e) {
            logger.error("L'initialisation de la clé publique et privée a échoué !", e);
            throw new RuntimeException();
        }
    }

    // getter setter ...


    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public static Logger getLogger() {
        return logger;
    }
}