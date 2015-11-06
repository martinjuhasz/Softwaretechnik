package de.teamrocket.relaxo.util.config;

/**
 * Exception die von der Config geworfen werden kann, wenn beim parsen der ConfigFile ein Fehler auftritt.
 * Von ConfigException abgeleitet.
 */
public class ConfigFileException extends ConfigException {
    public ConfigFileException(String msg) {
        super(msg);
    }
}
