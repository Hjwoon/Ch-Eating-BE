package com.ch_eatimg.ch_eating.domain;

import org.springframework.security.core.GrantedAuthority;

public enum RoleName implements GrantedAuthority{
    ROLE_ADMIN, ROLE_CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}