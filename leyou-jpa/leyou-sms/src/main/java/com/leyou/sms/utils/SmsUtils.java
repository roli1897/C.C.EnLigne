package com.leyou.sms.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leyou.sms.config.SmsProperties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtils {

    @Autowired
    private SmsProperties prop;

    //Nom du produit : Produit API de messagerie cloud, les développeurs n'ont pas besoin de le remplacer.
    static final String product = "Dysmsapi";
    //Nom de domaine du produit, les développeurs n'ont pas besoin de le remplacer.
    static final String domain = "dysmsapi.aliyuncs.com";

    static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);

    public SendSmsResponse sendSms(String phone, String code, String signName, String template) throws ClientException {

        //Cela permet l'ajustement autonome du délai d'attente.
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //Initialisation d'acsClient, la régionalisation n'est pas encore prise en charge
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                prop.getAccessKeyId(), prop.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //Assembler l'objet de requête - Voir la description détaillée dans la console - Contenu de la section de documentation
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        //Champ obligatoire : numéro de téléphone de destination.
        request.setPhoneNumbers(phone);
        //Champ obligatoire : signature SMS - peut être trouvé dans la console SMS
        request.setSignName(signName);
        //Champ obligatoire : modèle de SMS - peut être trouvé dans la console SMS
        request.setTemplateCode(template);
        //Optionnel : chaîne JSON pour le remplacement des variables dans le modèle.
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        //Optionnel - code d'extension pour les SMS entrants (les utilisateurs sans exigences particulières peuvent ignorer ce champ).
        //request.setSmsUpExtendCode("90997");

        //Optionnel : Le champ outId est un champ d'extension fourni aux partenaires commerciaux.
        // Il sera finalement renvoyé au demandeur dans le message de réception du SMS.
        request.setOutId("123456");

        //Conseil : Des exceptions peuvent être levées à cet endroit. Assurez-vous d'inclure une gestion appropriée des exceptions (catch)
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        logger.info("发送短信状态：{}", sendSmsResponse.getCode());
        logger.info("发送短信消息：{}", sendSmsResponse.getMessage());

        return sendSmsResponse;
    }
}