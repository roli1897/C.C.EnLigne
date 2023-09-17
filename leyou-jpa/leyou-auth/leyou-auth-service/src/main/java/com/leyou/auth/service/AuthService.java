package com.leyou.auth.service;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;

    public String accredit(String username, String password) {
        //1.Effectuer une recherche en fonction du nom d'utilisateur et du mot de passe
        User user = this.userClient.queryUser(username, password);
        //2.Déterminer user
        if (user == null){
            return null;
        }

        try {
            //3.jwtUtils génère un jeton de type JWT
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            return JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}