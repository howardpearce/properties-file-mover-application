package com.utils.Config;

import java.util.Properties;

/**
 * Accesses Properties class and returns configuration values in a controlled manner
 *
 * @author Howard Pearce
 */
public class ConfigurationManager {
    /**
     * Contains all configuration, managed by this class
     */
    Properties m_configuration;

    /**
     * Creates an instance of a ConfigurationManager
     *
     * @param configuration The pre-loaded configuration data
     */
    public ConfigurationManager(Properties configuration) {
        this.m_configuration = configuration;
    }

    /**
     * Provides a configuration item as a String
     *
     * @param key The configuration item name
     * @return the value of that configuration item
     * @throws ConfigurationException if the key is not found in configuration
     */
    public String getConfigItemAsString(String key) throws ConfigurationException {
        return (String) this.getConfigItem(key);
    }

    /**
     * Provides a configuration item as an Integer
     *
     * @param key The configuration item name
     * @return The value of that configuration item
     * @throws ConfigurationException If the configuration item could not be found
     * @throws NumberFormatException  If the configuration item could not be parsed into an Integer
     */
    public Integer getConfigItemAsInteger(String key) throws ConfigurationException, NumberFormatException {
        return Integer.valueOf((String) this.getConfigItem(key));
    }

    /**
     * Directly accesses configuration and validates config item exists
     *
     * @param key The configuration item name
     * @return Object representation of config item to be further converted
     * @throws ConfigurationException If the configuration item could not be found
     */
    private Object getConfigItem(String key) throws ConfigurationException {
        if (m_configuration.containsKey(key)) {
            return m_configuration.get(key);
        } else {
            throw new ConfigurationException("Common.Config item '" + key + "' could not be found.");
        }
    }
}

