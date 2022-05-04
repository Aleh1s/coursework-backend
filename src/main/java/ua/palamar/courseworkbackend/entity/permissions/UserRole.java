package ua.palamar.courseworkbackend.entity.permissions;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static ua.palamar.courseworkbackend.entity.permissions.UserPermission.*;

public enum UserRole {

    VIEWER(Sets.newHashSet(POST_READ)),
    USER(Sets.newHashSet(POST_READ, POST_CREATE, POST_DELETE, POST_UPDATE, ORDER_ITEM, ORDER_SERVICE, RENT_HOUSE)),
    ADMIN(Sets.newHashSet(POST_READ, POST_CREATE, POST_DELETE, POST_UPDATE, ORDER_ITEM, ORDER_SERVICE, RENT_HOUSE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permission) {
        this.permissions = permission;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<GrantedAuthority> getAuthorities () {
        return getPermissions().stream()
                .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission()))
                .collect(Collectors.toSet());
    }
}
