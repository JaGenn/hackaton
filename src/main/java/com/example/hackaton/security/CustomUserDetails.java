package com.example.hackaton.security;


import com.example.hackaton.web.model.entity.User;
import com.example.hackaton.web.model.entity.pollution.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;


@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {
    private final Long id;
    private final String username;
    private final String password;
    private final Role role;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

}
