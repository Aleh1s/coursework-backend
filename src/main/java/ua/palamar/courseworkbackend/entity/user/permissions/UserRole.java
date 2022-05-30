package ua.palamar.courseworkbackend.entity.user.permissions;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static ua.palamar.courseworkbackend.entity.user.permissions.UserPermission.*;

public enum UserRole {

    USER(Sets.newHashSet(
            ADVERTISEMENT_READ,
            ADVERTISEMENT_CREATE,
            ADVERTISEMENT_DELETE,
            ORDER_READ,
            ORDER_CHANGE,
            ORDER_MAKE,
            USER_UPDATE
    )),
    ADMIN(Sets.newHashSet(
            MODERATION,
            USER_WRITE,
            USER_UPDATE,
            FEEDBACK_READ,
            ADVERTISEMENT_READ
    ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permission) {
        this.permissions = permission;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission()))
                .collect(Collectors.toSet());
    }
}
