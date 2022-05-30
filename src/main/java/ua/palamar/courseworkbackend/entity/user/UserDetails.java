package ua.palamar.courseworkbackend.entity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;

import java.util.Collection;
@AllArgsConstructor
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private String email;
    private String password;
    private UserRole role;
    private UserStatus status;

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
        return status.equals(UserStatus.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(UserStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status.equals(UserStatus.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return status.equals(UserStatus.ACTIVE);
    }

}
