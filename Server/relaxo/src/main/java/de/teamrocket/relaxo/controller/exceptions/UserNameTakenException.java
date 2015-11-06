package de.teamrocket.relaxo.controller.exceptions;

public class UserNameTakenException extends Exception {
    public UserNameTakenException(String message) {
        super(message);
    }
}
