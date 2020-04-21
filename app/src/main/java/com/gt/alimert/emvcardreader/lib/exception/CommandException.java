package com.gt.alimert.emvcardreader.lib.exception;

/**
 * @author AliMertOzdemir
 * @class CommandException
 * @created 17.04.2020
 * @copyright © GARANTI TEKNOLOJI
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
