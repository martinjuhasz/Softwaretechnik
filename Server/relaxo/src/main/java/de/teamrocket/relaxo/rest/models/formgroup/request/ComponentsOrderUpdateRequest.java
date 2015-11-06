package de.teamrocket.relaxo.rest.models.formgroup.request;

import java.util.List;

/**
 * Payload-Klasse eines ComponentsOrderUpdate Requests
 */
public class ComponentsOrderUpdateRequest {

    private List<ComponentOrderUpdateRequest> components;

    public List<ComponentOrderUpdateRequest> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentOrderUpdateRequest> components) {
        this.components = components;
    }
}
