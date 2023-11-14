package com.farmted.authservice.global.security;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.enums.RoleEnums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Pass pass;
    private final String email;
    private final String uuid;

    public Pass getPass() {return pass;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        RoleEnums role = pass.getRole();
        String authority = role.getKey();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.uuid;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
