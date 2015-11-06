package de.teamrocket.relaxo.util.keyboardinterface;

/**
 * Exception die vom KeyboardInterface geworfen werden kann.
 * Von RuntimeException abgeleitet.
 */
public class KeyboardInterfaceException extends RuntimeException {
    public KeyboardInterfaceException(String msg) {
        super(msg);
    }
}