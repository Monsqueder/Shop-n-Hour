package com.example.demo.services;

import com.example.demo.models.Consumer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class ConsumerDetails implements UserDetails {

    private Consumer consumer;



    public ConsumerDetails(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return consumer.getRoles();
    }

    @Override
    public String getPassword() {
        return consumer.getHashPassword();
    }

    @Override
    public String getUsername() {
        return consumer.getEmail();
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
        return consumer.getIsActivated();
    }
}
