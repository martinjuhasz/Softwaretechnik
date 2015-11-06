package de.teamrocket.relaxo.models.taskcomponent;


/**
 * TaskComponent_Text ist eine konkrete Auspraegung der Klasse TaskComponent.
 */
public class TaskComponentText extends TaskComponent {

    // Overrides

    /**
     * Der Default-Wert.
     */
    private String defaultValue;

    /**
     * Ein REGEX zum ueberpruefen der Eingabe.
     * Zum ausschalten der Ueberpruefung muss dieses NULL sein.
     */
    private String regex;

    public TaskComponentText() {
        this.setType("TEXTLABEL");
    }

    /**
     * Gibt den Defaultwert zurück.
     *
     * @return Defaultwert der Komponente
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Setzt den Defaultwert der Komponente
     *
     * @param defaultValue Defaultwert der Komponente
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    // Setter

    /**
     * Gibt den Regulärer Ausdruck der Komponente zurück.
     *
     * @return Regulärer Ausdruck der Komponente
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Setzt den Regulären Ausdruck der Komponente
     *
     * @param regex Regulärer Ausdruck der Komponente
     */
    public void setRegex(String regex) {
        this.regex = regex;
    }

}