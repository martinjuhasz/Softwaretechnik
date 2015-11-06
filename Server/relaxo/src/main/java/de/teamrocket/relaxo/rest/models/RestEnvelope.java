package de.teamrocket.relaxo.rest.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Wrapperklasse um Enveloping der Rest-Schnittstelle zu ermöglichen
 *
 * @param <T> KlassenTyp der Daten die im `data`Attribut gehalten werden sollen
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestEnvelope<T> {

    /**
     * ErrorObjekt bei Fehlerrückgabe
     */
    private ErrorResponse error;
    /**
     * Rückgabedaten bei Erfolg
     */
    private T data;


    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
