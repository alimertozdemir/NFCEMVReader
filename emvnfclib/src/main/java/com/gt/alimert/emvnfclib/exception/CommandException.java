package com.gt.alimert.emvnfclib.exception;

/**
 * @author AliMertOzdemir
 * @class CommandException
 * @created 17.04.2020
 */
public class CommandException extends Exception {

    public CommandException() {}

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
