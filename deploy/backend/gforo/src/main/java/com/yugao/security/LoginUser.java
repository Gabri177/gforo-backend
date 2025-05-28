package com.yugao.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Data
public class LoginUser implements UserDetails {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean isSuperAdmin;
    private Boolean isAdmin;
    private Integer userLevel;
    private String deviceId;
    private Collection<? extends GrantedAuthority> authorities;

    public LoginUser(Long userId, String username, String password, Integer userLevel, String deviceId, List<String> perms) {
        this.id = userId;
        this.username = username;
        this.password = password;
        this.userLevel = userLevel;
        this.isSuperAdmin = userLevel == 0;
        this.isAdmin = userLevel == 1;
        this.deviceId = deviceId;
        this.authorities = perms.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean hasAuthority(String code) {
        if (this.authorities == null) return false;
        return this.authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals(code));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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
