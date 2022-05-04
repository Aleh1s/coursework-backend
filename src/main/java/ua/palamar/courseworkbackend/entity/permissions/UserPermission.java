package ua.palamar.courseworkbackend.entity.permissions;

public enum UserPermission {

    POST_READ("post:read"),
    POST_DELETE("post:delete"),
    POST_CREATE("post:create"),
    POST_UPDATE("post:update"),
    ORDER_ITEM("order:item"),
    ORDER_SERVICE("order:service"),
    RENT_HOUSE("rent:house");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
