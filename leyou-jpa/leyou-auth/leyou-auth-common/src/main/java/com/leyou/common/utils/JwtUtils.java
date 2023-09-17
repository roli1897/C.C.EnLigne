package com.leyou.common.utils;

import com.leyou.common.pojo.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtUtils {
    /**
     * Chiffrer le jeton avec une clé privée
     *
     * @param userInfo      Les données contenues dans la charge utile.
     * @param privateKey    le clé de privé
     * @param expireMinutes Temps d'expiration, en secondes.
     * @return
     * @throws Exception
     */
    public static String generateToken(UserInfo userInfo, PrivateKey privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                .claim(JwtConstans.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstans.JWT_KEY_USER_NAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * Chiffrer le jeton avec une clé privée
     *
     * @param userInfo      Les données dans la charge utile
     * @param privateKey    Tableau d'octets de clé privée
     * @param expireMinutes Temps d'expiration, unité en secondes
     * @return
     * @throws Exception
     */
    public static String generateToken(UserInfo userInfo, byte[] privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                .claim(JwtConstans.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstans.JWT_KEY_USER_NAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, RsaUtils.getPrivateKey(privateKey))
                .compact();
    }

    /**
     * Décoder le jeton avec la clé publique
     *
     * @param token     Le jeton présent dans la demande de l'utilisateur
     * @param publicKey le clé de public
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * Décoder le jeton avec la clé publique
     *
     * @param token     Le jeton présent dans la demande de l'utilisateur
     * @param publicKey Tableau d'octets de clé publique
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtils.getPublicKey(publicKey))
                .parseClaimsJws(token);
    }

    /**
     * Obtenir les informations de l'utilisateur dans le jeton
     *
     * @param token     jeton dans la demande de l'utilisateur
     * @param publicKey le clé de public
     * @return les information d'utilisateur
     * @throws Exception
     */
    public static UserInfo getInfoFromToken(String token, PublicKey publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstans.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstans.JWT_KEY_USER_NAME))
        );
    }

    /**
     * Obtenir les informations de l'utilisateur dans le jeton
     *
     * @param token     le jeton dans la demande de l'utilisateur
     * @param publicKey le clé de public
     * @return les information d'utilisateur
     * @throws Exception
     */
    public static UserInfo getInfoFromToken(String token, byte[] publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstans.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstans.JWT_KEY_USER_NAME))
        );
    }
}