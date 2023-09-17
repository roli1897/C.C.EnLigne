package com.leyou.cart.interceptor;

import com.leyou.cart.config.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter { //implements HandlerInterceptor nécessite la redéfinition de trois méthodes

    @Autowired
    private JwtProperties jwtProperties;

    //public static UserInfo userInfo; Il s'agit d'une question de sécurité liée aux threads ×
    // Définir un domaine de thread pour stocker l'utilisateur connecté
    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Récupérer le jeton depuis le cookie
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        if(StringUtils.isBlank(token)) {
            //Non connecté, renvoie un code 401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        //Décoder le jeton, obtenir les informations de l'utilisateur
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
        if (userInfo == null) {
            return false;
        }
        //Placer userInfo dans une variable locale de thread
        THREAD_LOCAL.set(userInfo);

        return true;
    }
    public static UserInfo getUserInfo() {
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //Vider la variable locale du thread, car des threads provenant du pool de threads Tomcat ne se terminent pas et ne libèrent donc pas la mémoire du thread
        THREAD_LOCAL.remove();
    }
}