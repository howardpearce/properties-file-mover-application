package com.utils.Config;

import com.utils.Files.FileUtils;
import com.utils.Logger.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Responsible for reading in raw configuration file and converting to usable Properties object
 *
 * @author Howard Pearce
 */
public class ConfigLoader {
    /**
     * File location of config as String
     */
    private String m_filepath;

    /**
     * Create a ConfigLoader instance. Should be one per config file
     *
     * @param filepath The filesystem location of the desired configuration file
     */
    public ConfigLoader(String filepath) {
        this.m_filepath = filepath;
    }

    /**
     * Retrieves actual configuration file and converts it to usable ConfigurationManager
     *
     * @return ConfigurationManager for accessing config data
     * @throws IOException If config file could not be read
     */
    public ConfigurationManager getConfiguration() throws IOException {
        Logger.logInfo("Loading configuration file '" + m_filepath + "'");
        // open up the provided configuration file as a properties file
        Properties configuration = FileUtils.getFileAsProperties(m_filepath);
        // Return a wrapper for the Properties object
        return new ConfigurationManager(configuration);

    }

    /**
     * Accessor for filepath
     *
     * @return String representation of config filepath
     */
    public String getFilepath() {
        return m_filepath;
    }

    /**
     * Change configuration filepath
     *
     * @param filepath the new configuration filepath location
     */
    public void setFilepath(String filepath) {
        this.m_filepath = filepath;
    }
}
