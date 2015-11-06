package de.teamrocket.relaxo.rest.models.usermanagement.response;

import de.teamrocket.relaxo.models.usermanagement.UserGroup;

/**
 * Antwort der REST-Anfrage für eine Benutzergruppe
 */
public class UserGroupResponse {

    /**
     * ID der Benutzergruppe.
     */
    private int id;

    /**
     * Name der Benutzergruppe.
     */
    private String name;

    /**
     * Erstellt eine Response anhander der Benutzergruppe.
     *
     * @param group Die Benutzergruppe.
     */
    public UserGroupResponse(UserGroup group) {
        this.id = group.getId();
        this.name = group.getName();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
