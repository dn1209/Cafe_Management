package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mặc định sẽ để tất cả là ROLE_USER. Để demo cho đơn giản.
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }



    public Long getId(){
        return user.getUserId();
    }
    public String getEmail(){
        return user.getEmail();
    }
    public String getDisplayName(){
        return user.getDisplayName();
    }
    public String getUserStatus(){
        return user.getUserStatus();
    }

    public Long getStoreId(){
        return user.getStore().getStoreId();
    }
    public LocalDate getCreatedAt(){
        return user.getCreatedAt();
    }
    public LocalDate getUpdatedAt(){
        return user.getUpdatedAt();

    }
    public LocalDate getLoginDate(){
        return user.getLogined();
    }
    public int getParentId(int a){
        return a;
    }
    public String getIpLogged(){
        return "45.124.94.221";
    }
    public String getRecode(){
        return "";
    }
    public String codeTimeOut(){
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}