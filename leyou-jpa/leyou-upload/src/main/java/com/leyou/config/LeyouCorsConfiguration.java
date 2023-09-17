package com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration //résoudre les problèmes de CORS
public class LeyouCorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        //initialiser l'objet de configuration CORS
        CorsConfiguration configuration = new CorsConfiguration();
        /**
         * Les domaines autorisés pour les requêtes cross-origin ne peuvent pas être définis comme "*",
         * car cela signifie que tous les domaines sont autorisés à accéder en mode cross-origin, y compris le transfert de cookies
         */
        configuration.addAllowedOrigin("http://manage.leyou.com");
        configuration.setAllowCredentials(true); //autoriser le transport de cookies
        configuration.addAllowedMethod("*"); //représente toutes les méthodes de requête:get post put delete
        configuration.addAllowedHeader("*");//représente l'envoi de toutes les informations d'en-tête
        //Initialiser l'objet de configuration CORS source, ajouter des chemins de mappage, intercepter toutes les requêtes.
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);
        //Renvoyer une instance de corsFilter, avec le paramètre : objet de configuration CORS
        return new CorsFilter(configurationSource);
    }
}