package com.leyou.order.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tb_order_status")
public class OrderStatus {

    @Id
    private Long orderId;
    /**
     * Phase initiale : 1. Non payé, non expédié ; Initialisation de toutes les données
     * Phase de paiement : 2. Payé, non expédié ; Modification de l'heure de paiement
     * Phase d'expédition : 3. Expédié, non confirmé ; Modification de l'heure d'expédition, nom de la logistique, numéro de suivi
     * Phase de réussite : 4. Confirmé, non évalué ; Modification de l'heure de fin de transaction
     * Phase de clôture : 5. Fermé ; Modification de l'heure de mise à jour, heure de clôture de la transaction
     * Phase d'évaluation : 6. Évalué"
     */
    private Integer status;

    private Date createTime;

    private Date paymentTime;

    private Date consignTime;// le date de expédition

    private Date endTime;// Date de fin de la transaction

    private Date closeTime;// Heure de clôture de la transaction

    private Date commentTime;// Heure d'évaluation

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
}