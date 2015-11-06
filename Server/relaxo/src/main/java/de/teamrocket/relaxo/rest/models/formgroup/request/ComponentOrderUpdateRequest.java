package de.teamrocket.relaxo.rest.models.formgroup.request;

/**
 * Payload-Klasse eines ComponentOrderUpdate Requests
 */
public class ComponentOrderUpdateRequest {

    private int id;
    private int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
