package ua.palamar.courseworkbackend.entity.user.permissions;

public enum UserPermission {

    ADVERTISEMENT_READ("advertisement:read"),
    ADVERTISEMENT_CREATE("advertisement:create"),
    ADVERTISEMENT_DELETE("advertisement:delete"),
    ORDER_READ("order:read"),
    ORDER_MAKE("order:make"),
    ORDER_CHANGE("order:change"),
    USER_UPDATE("user:update"),
    USER_WRITE("user:block"),
    MODERATION("moderation"),
    FEEDBACK_READ("feedback:read");


    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
