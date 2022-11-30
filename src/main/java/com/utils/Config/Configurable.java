package com.utils.Config;

/**
 * Functional interface for reading configuration files
 *
 * @author Howard Pearce
 */
public interface Configurable {
    /**
     * Read configuration file and parse needed configuration values
     *
     * @throws ConfigurationException if configuration cannot be read
     */
    void readConfiguration(ConfigurationManager manager) throws ConfigurationException;
}
