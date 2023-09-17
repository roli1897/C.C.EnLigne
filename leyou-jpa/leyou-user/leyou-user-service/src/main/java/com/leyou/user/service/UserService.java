package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;

import com.leyou.user.pojo.User;
import com.leyou.user.repository.UserRepository;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    /**
     * Vérifier la disponibilité du nom d'utilisateur et du numéro de téléphone
     *
     * @param data
     * @param type
     * @return
     */
    public Boolean checkUser(String data, Integer type) {
        if (type != 1 && type != 2) {
            return null;
        } else {
            Specification<User> spec = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder
                        criteriaBuilder) {
                    if (type == 1) {
                        return criteriaBuilder.equal(root.get("username"), data);
                    } else {
                        return criteriaBuilder.equal(root.get("phone"), data);
                    }
                }
            };
            return this.userRepository.count(spec) == 0;
        }
    }

        /**
         * Envoyer le code de vérification au téléphone de l'utilisateur
         *
         * @param phone
         * @return
         */
        public void sendVerifyCode (String phone){
            if (StringUtils.isBlank(phone)) {
                return;
            }
            //Générer un code de vérification
            String code = NumberUtils.generateCode(6);
            try {
                //Enregistrer le code dans Redis
                Map<String, String> msg = new HashMap<>();
                msg.put("phone", phone);
                msg.put("code", code);
                this.amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "verifycode.sms", msg);
                //Enregistrer le code dans Redis
                this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            } catch (Exception e) {
                LOGGER.error("Le message n'a pas été envoyé. Téléphone : {}, Code : {}.", phone, code);
            }
        }

        /**
         * Enregistrer un nouvel utilisateur
         *
         * @param user
         * @param code
         * @return
         */
        public void register (User user, String code){
            //1.Rechercher le code dans Redis
            String redisCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
            //2. Vérifier le code
            if (!StringUtils.equals(code, redisCode)) {
                throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
            }
            //3.Générer un sel
            String salt = CodecUtils.generateSalt();
            user.setSalt(salt);
            //4.Ajouter le sel et chiffrer
            user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
            //5.Ajouter un nouvel utilisateur
            user.setId(null);
            user.setCreated(new Date());
            this.userRepository.save(user);
            //6. Supprime le code depuis Redis
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }

        /**
         * Rechercher l'utilisateur par nom
         *
         * @param username
         * @param password
         * @return
         */
        public User queryUser (String username, String password){
            Specification<User> spec = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("username"), username);
                }
            };
            Optional<User> optionalUser = this.userRepository.findOne(spec);
            //Vérifier si l'utilisateur existe
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                //Obtenir le sel, ajouter le sel et chiffrer le mot de passe inséré
                password = CodecUtils.md5Hex(password, user.getSalt());
                //Le comparer avec le mot de passe dans la base de données
                if (StringUtils.equals(password, user.getPassword())) {
                    return user;
                }
            }
            return null;
        }
    }