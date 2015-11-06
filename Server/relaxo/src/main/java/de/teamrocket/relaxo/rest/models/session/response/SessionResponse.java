package de.teamrocket.relaxo.rest.models.session.response;

import de.teamrocket.relaxo.models.usermanagement.User;

/**
 * Antwort auf Sessionanfragen des REST-Servers
 */
public class SessionResponse {

    //

    /**
     * das neue Sessiontoken
     */
    private String token;

    private boolean admin;

    private int id;

    //

    /**
     * leerer Konstruktor
     */
    public SessionResponse() {

    }

    /**
     * Erstellt eine SessionResponse mit Token
     *
     * @param token das zu speichernde Token
     */
    public SessionResponse(String token, User user) {
        this.token = token;
        this.id = user.getId();
        this.admin = user.isAdmin();
    }

    //

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
