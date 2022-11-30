package com.utils.Config;

/**
 * Exception to be thrown when configuration files are invalid
 *
 * @author Howard Pearce
 */
public class ConfigurationException extends Exception {
    /**
     * Creates instance of ConfigurationException
     *
     * @param message the reason for the exception for users to read
     */
    public ConfigurationException(String message) {
        super(message);
    }
}
