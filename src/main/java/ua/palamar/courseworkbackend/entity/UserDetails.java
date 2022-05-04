package ua.palamar.courseworkbackend.entity;

import org.springframework.security.core.GrantedAuthority;
import ua.palamar.courseworkbackend.entity.permissions.UserRole;
import ua.palamar.courseworkbackend.entity.permissions.UserStatus;

import java.util.Collection;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private String email;
    private String password;
    private UserStatus status;
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
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
