package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnum {
    BRAND_NOT_FOUND(404,"Marque inexistante"),
    CATEGORY_NOT_FOUND(404,"Catalogue introuvable"),
    BRAND_SAVE_ERROR(500,"Serveur : La marque n'est pas enregistrée"),
    CATEGORY_BRAND_SAVE_ERROR(500,"Serveur : Le catalogue de marque n'est pas enregistré"),
    UPLOAD_FILE_ERROR(500,"Chargement de fichier échoué"),
    INVALID_FILE_TYPE(400,"Type de fichier invalide"),
    SPEC_GROUP_NOT_FOUND(404,"Groupe de spécifications n'existe pas"),
    SPEC_PARAM_NOT_FOUND(404,"Le paramètre de spécification n'a pas été trouvé"),
    GOODS_NOT_FOUND(404,"L'article n'a pas été trouvé"),
    GOODS_SAVE_ERROR(500,"Échec de l'enregistrement de l'article"),
    GOODS_DETAIL_NOT_FOUND(404,"Les détails de l'article n'existent pas"),
    GOODS_SKU_NOT_FOUND(404,"SKU inexistan"),
    GOODS_STOCK_NOT_FOUND(404,"Le stock de l'article n'a pas été trouvé"),
    GOODS_UPDATE_ERROR(500,"Échec de la mise à jour de l'article"),
    GOODS_ID_CANNOT_BE_NULL(400,"L'ID de l'article ne peut pas être nul"),
    INVALID_USER_DATA_TYPE(400,"Le type de données de l'utilisateur est invalide"),
    INVALID_VERIFY_CODE(400,"Le code de vérification est invalide"),
    INVALID_USERNAME_PASSWORD(400,"le username et le password de vérification est invalide"),
    CREATE_TOKEN_ERROR(500,"Échec de la création du token"),
    UN_AUTHORIZED(403,"Pas autorisé"),
    CART_NOT_FOUND(404,"Le panier n'existe pas"),
    STOCK_NOT_FOUND(404,"Le stock n'est pas trouvé" ),
    RECEIVER_ADDRESS_NOT_FOUND(404,"L'adresse du destinataire n'est pas trouvée" ),
    CREATED_ORDER_ERROR(500,"Créer la commande avec échec" ),
    STOCK_NOT_ENOUGH(500, "stock insuffisant"),
    ORDER_NOT_FOUND(404,"La commande n'existe pas" ),
    ORDER_DETAIL_NOT_FOUND(404,"Détail de la commande introuvable" ),
    ORDER_STATUS_NOT_FOUND(404,"L'état de la commande n'est pas trouvé" ),
    WX_PAY_ORDER_FAIL(500,"WeChat Pay a échoué" ),
    ORDER_STATUS_ERROR(400,"L'état de la commande est anormal" ),
    INVALID_SIGN_ERROR(400,"Signature invalide" ),
    INVALID_ORDER_PARAM(400,"Le paramètre du montant est invalide" ),
    UPDATE_ORDER_STATUS_ERROR(500,"Échec de la mise à jour de l'état de la commande" ),
    IS_NOT_AN_ADMIN(500,"L'utilisateur n'est pas administrateur");
    private int code;
    private String msg;
}