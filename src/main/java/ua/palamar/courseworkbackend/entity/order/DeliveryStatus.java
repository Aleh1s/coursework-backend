package ua.palamar.courseworkbackend.entity.order;

public enum DeliveryStatus {

    IN_ROAD("in_road"),
    IN_PROCESS("in_process"),
    DELIVERED("delivered");

    private final String qualifier;

    DeliveryStatus(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getQualifier() {
        return qualifier;
    }
}
