package de.teamrocket.relaxo.models.usermanagement;

/**
 * Benutzergruppen werden durch diese Klasse repräsentiert.
 */
public class UserGroup {

    /**
     * Die ID der Gruppe.
     */
    private int id;

    /**
     * Die Bezeichnung der Gruppe.
     */
    private String name;

    public UserGroup() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Gruppe: " + this.name + " | ID: " + this.id;
    }
}