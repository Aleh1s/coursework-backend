package ua.palamar.courseworkbackend.entity.advertisement;

public enum Category {

    ITEM("item"),
    SERVICE("service"),
    HOUSE("house");

    private final String qualifier;
    Category(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getQualifier() {
        return qualifier;
    }
}
