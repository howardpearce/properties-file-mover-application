package com.utils;

import com.client.ClientApplication;
import com.utils.Logger.Logger;
import com.utils.Config.ConfigLoader;
import com.utils.Config.Configurable;
import com.utils.Config.ConfigurationException;
import com.utils.Config.ConfigurationManager;

import java.io.IOException;

/**
 * Shared functionality between both the server and client application
 *
 * @author Howard Pearce
 */
public abstract class BaseApplication implements Configurable {
    /**
     * String path of the directory that will be acted on.
     */
    protected String m_directory = null;
    /**
     * Name of the application, either 'client' or 'server' currently.
     */
    protected String m_applicationName = null;
    /**
     * Retrieves configuration file.
     */
    protected ConfigLoader m_configurationLoader = null;
    /**
     * Wrapper for accessing configuration, parses and handles validity checking.
     */
    protected ConfigurationManager m_configurationManager = null;

    /**
     * Creates a Common.BaseApplication
     *
     * @param configPath A string representation of the configuration file location
     * @param name       The name of the application. Used in configuration.
     * @throws IOException If configuration file is inaccessible
     */
    public BaseApplication(String configPath, String name) throws IOException {
        this.m_configurationLoader = new ConfigLoader(configPath);
        this.m_configurationManager = m_configurationLoader.getConfiguration();
        this.m_applicationName = name;
    }

    /**
     * Change the directory that the service is using
     *
     * @param directory A string representation of the directory path
     */
    public void setDirectory(String directory) {
        this.m_directory = directory;
    }

    /**
     * Retrieve the directory the service is using
     *
     * @return A string representation of the directory path
     */
    public String getDirectory() {
        return this.m_directory;
    }

    /**
     * Read in all configuration for this service
     *
     * @param configuration Access to the configuration file
     * @throws ConfigurationException If configuration is missing items or cannot be parsed
     */
    public void readConfiguration(ConfigurationManager configuration) throws ConfigurationException {
        m_directory = configuration.getConfigItemAsString(m_applicationName + ".directory");
        Logger.logDebug("Loaded directory as " + m_directory);
    }

    /**
     * Instantiates the application and applies full configuration (dependent on subclass overriding readConfiguration method)
     *
     * @param configPath String representation of the filepath for the application's configuration file
     * @return new BaseApplication object, null if configuration fails
     */
    public static BaseApplication getConfiguredApplication(String configPath) {
        try {
            // TODO: Should not be client application
            ClientApplication application = new ClientApplication(configPath);
            application.readConfiguration();
            Logger.logInfo("Read configuration sucessfully.");
            return application;
        } catch (IOException | ConfigurationException e) {
            Logger.logError("Unable to read provided configuration: " + e.getMessage());
            return null;
        }
    }
}
