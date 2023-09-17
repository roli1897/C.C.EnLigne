package com.leyou.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration //Partage des ressources entre origines multiples
public class LeyouCorsConfiguration {
    @Bean
    public CorsFilter corsFilter() {
        //Initialiser l'objet de configuration
        CorsConfiguration configuration = new CorsConfiguration();
        //si des cookies doivent être envoyés, le chemin ne peut pas être *
        configuration.addAllowedOrigin("http://manage.leyou.com");
        configuration.addAllowedOrigin("http://www.leyou.com");
        configuration.setAllowCredentials(true); //Permettre les cookies
        configuration.addAllowedMethod("*"); //pour toutes les méthodes：get post put delete
        configuration.addAllowedHeader("*");//Prendre en charge tous les types d'en-têtes
        //Filtre toutes les routes
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);
        //Renvoyer une instance de corsFilter
        return new CorsFilter(configurationSource);
    }
}