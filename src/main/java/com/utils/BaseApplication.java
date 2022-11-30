package com.utils;

import com.client.ClientApplication;
import com.utils.Files.FileUtils;
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
    public BaseApplication(String configPath, String name) throws ConfigurationException {
        this.m_configurationLoader = new ConfigLoader(configPath);
        try {
            this.m_configurationManager = m_configurationLoader.getConfiguration();
        } catch (IOException e) {
            Logger.logError("Unable to load provided configuration file '" + configPath + "'. Cannot proceed. ");
            throw new ConfigurationException("Unable to load provided configuration file '" + configPath + "'");
        }
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
        if (!FileUtils.doesFileExist(m_directory)) {
            throw new ConfigurationException("Provided watch directory '" + m_directory +  "' does not exist.");
        }
    }
}
