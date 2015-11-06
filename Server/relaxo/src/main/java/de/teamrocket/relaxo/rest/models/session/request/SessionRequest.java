package de.teamrocket.relaxo.rest.models.session.request;

/**
 * Wrapper-Klasse für Session Anfragen der REST-API
 */
public class SessionRequest {

    //

    /**
     * Username des Benutzers
     */
    private String username;
    /**
     * Passwort des Benutzers
     */
    private String password;

    //

    /**
     * Leerer Konstruktor
     */
    public SessionRequest() {

    }

    //

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
