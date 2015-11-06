package de.teamrocket.relaxo.models.usermanagement;

/**
 * Benutzer des Systems werden in dieser Klasse repräsentiert.
 */
public class User {

    /**
     * Beinhaltet die persönliche und einzigartige User-Identifikationsnummer;
     */
    private int id;

    /**
     * Der Login-Name des Users.
     */
    private String username;

    /**
     * Das Passwort (noch in klartext) des Benutzers.
     */
    private String password;

    /**
     * Der Vorname des Benutzers.
     */
    private String prename;

    /**
     * Der Nachname des Benutzers.
     */
    private String name;

    /**
     * Flag, ob User Admin ist
     */
    private boolean admin;

    /**
     * Flag, ob user aktive ist
     */
    private boolean active;

    public User() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrename() {
        return this.prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
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

    public boolean isAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User: " + this.username + " | ID: " + this.id + " | Vorname: " + this.prename + " | Nachname: " + this.name + " | Admin: " + this.admin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}