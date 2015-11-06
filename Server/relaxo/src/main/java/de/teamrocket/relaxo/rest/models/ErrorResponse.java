package de.teamrocket.relaxo.rest.models;

import java.util.LinkedList;
import java.util.List;

/**
 * Rückgabeobjekt des REST-Servers bei Fehlern
 */
public class ErrorResponse {

    /**
     * der Fehlercode
     */
    private String code;
    /**
     * Die Fehlernachricht
     */
    private String message;

    /**
     * Konstruktor des Error Objektes
     *
     * @param code    Code des Fehlers
     * @param message Nachricht des Fehlers
     */
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
