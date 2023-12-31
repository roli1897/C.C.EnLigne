package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4, max = 30, message = "La longitud de nombre tiene que ser entre 4 y 30")
    private String username;

    @Length(min = 4, max = 40, message = "La longitud de clave tiene que ser  entre 4 y 10")
    @JsonIgnore //Lors de la sérialisation de l'objet en chaîne JSON, ignorer cette propriété
    private String password;

    @Pattern(regexp = "^1[356789]\\d{9}$", message = "Número de móvil es ilegal")
    private String phone;

    private Date created;// le date de création

    @JsonIgnore
    private String salt;// Le sel du mot de passe

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}