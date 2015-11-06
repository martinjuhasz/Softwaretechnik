package de.teamrocket.relaxo.util.config;

/**
 * Exception die von der Config geworfen werden kann, wenn beim Casten von Variablen ein Fehler auftritt.
 * Von ConfigException abgeleitet.
 */
public class ConfigCastException extends ConfigException {
    public ConfigCastException(String msg) {
        super(msg);
    }
}
