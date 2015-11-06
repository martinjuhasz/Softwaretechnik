package de.teamrocket.relaxo.models.taskcomponent;

/**
 * ENUM der alle implementierten Component Typen beinhaltet
 */
public enum ComponentType {
    TYPE_TEXT("TaskComponentText"),
    TYPE_INTEGER("TaskComponentInteger"),
    TYPE_DATE("TaskComponentDate"),
    TYPE_FLOAT("TaskComponentFloat");

    private final String text;

    /**
     * Initialisiert einen ComponentType ENUM
     *
     * @param text die String-Repräsentation des Enums
     */
    private ComponentType(final String text) {
        this.text = text;
    }

    /**
     * Gibt einen Enum anhand eines Strings zurück
     *
     * @param text String-Repräsentation des gewünschten Enums
     * @return der Enum
     */
    public static ComponentType fromString(String text) {
        if (text != null) {
            for (ComponentType other : ComponentType.values()) {
                if (text.equalsIgnoreCase(other.text)) {
                    return other;
                }
            }
        }
        throw new IllegalArgumentException("ComponentType: No constant with text " + text + " found");
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}