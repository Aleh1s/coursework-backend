package ua.palamar.courseworkbackend.entity;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ua.palamar.courseworkbackend.entity.permissions.UserRole;
import ua.palamar.courseworkbackend.entity.permissions.UserStatus;

import java.util.Collection;

import static ua.palamar.courseworkbackend.entity.permissions.UserStatus.*;
@AllArgsConstructor
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private String email;
    private String password;
    private UserStatus status;
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status.equals(ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status.equals(ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return status.equals(ACTIVE);
    }
}
