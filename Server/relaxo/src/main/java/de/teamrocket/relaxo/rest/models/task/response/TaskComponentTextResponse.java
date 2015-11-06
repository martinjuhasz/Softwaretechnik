package de.teamrocket.relaxo.rest.models.task.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponentText;


/**
 * Text-Komponente fuer die Antwort des REST-Servers auf Anfragen nach Taskdetails
 */
public class TaskComponentTextResponse extends TaskComponentResponse {

    /**
     * Der Default-Wert.
     */
    private String defaultValue;
    /**
     * Ein REGEX zum ueberpruefen der Eingabe.
     * Zum ausschalten der Ueberpruefung muss dieses NULL sein.
     */
    private String regex;

    /**
     * Leerer Konstruktor
     */
    public TaskComponentTextResponse() {
    }

    /**
     * Konstruktor zum befüllen des Responses
     *
     * @param taskComponent TaskComponent aus der die Response erstellt werden soll
     */
    public TaskComponentTextResponse(TaskComponentText taskComponent) {
        super(taskComponent);
        this.defaultValue = taskComponent.getDefaultValue();
        this.regex = taskComponent.getRegex();
    }

    @JsonProperty("default")
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
