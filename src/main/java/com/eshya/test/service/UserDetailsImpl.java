package com.eshya.test.service;

import com.eshya.test.model.UserPortal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Set<SimpleGrantedAuthority> roles;

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String password, Set<SimpleGrantedAuthority> roles) {
        this.id = id;
        this.username = username;
//        this.authorities = authorities;
        this.password = password;
        this.roles = roles;
    }

    public static UserDetailsImpl build(UserPortal userPortal) {

        return new UserDetailsImpl(userPortal.getId(), userPortal.getUsername(), userPortal.getPassword(),getAuthority(userPortal));
    }


    private static Set<SimpleGrantedAuthority> getAuthority(UserPortal user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return authorities;
    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }

    public Long getId() {
        return id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
