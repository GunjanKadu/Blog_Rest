package com.api.rest;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.Base64;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BlogUser implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int age;

    @Enumerated(EnumType.STRING)
    private Role role;

    public String decodedPassword() {
        byte[] decodedPasswordBytes = Base64.getDecoder().decode(this.password);
        return new String(decodedPasswordBytes);
    }

    public void setPassword(String password) {
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
        ;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
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
