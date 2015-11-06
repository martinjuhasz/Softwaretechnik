package de.teamrocket.relaxo.util.config;

/**
 * Exception die von der Config geworfen werden kann.
 * Von RuntimeException abgeleitet.
 */
public class ConfigException extends RuntimeException {
    public ConfigException(String msg) {
        super(msg);
    }
}
