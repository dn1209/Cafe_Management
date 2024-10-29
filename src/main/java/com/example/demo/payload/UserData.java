package com.example.demo.model.payload;

import com.example.demo.model.CustomUserDetails;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserData {
    private String id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private String display_name;
    private String user_status;
    private String store_id;
    private LocalDate created;
    private LocalDate updated;
    private LocalDate logined;
    private String parent_id;
    private String ip_logged;
    private String recode;
    private String code_time_out;
    private String token_login;

    public UserData(CustomUserDetails user, String a, String token) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.salt = "qUquj@krSb!F66CCwr!kYoWN7A)XnMlQRv6ySRQ#I2rG)FEwRp3yoo3TDUKgDrswx876M";
        this.email = user.getEmail();
        this.display_name = user.getDisplayName();
        this.user_status = user.getUserStatus();
        this.store_id = String.valueOf(user.getStoreId());
        this.created = user.getCreatedAt();
        this.updated = user.getUpdatedAt();
        this.logined = user.getLoginDate();
        this.parent_id = a;
        this.ip_logged = user.getIpLogged();
        this.recode = user.getRecode();
        this.code_time_out = user.codeTimeOut();
        this.token_login = token;

    }
}