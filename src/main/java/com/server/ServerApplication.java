package com.server;

import com.utils.BaseApplication;
import com.utils.Files.FileUtils;
import com.utils.Files.PropertiesFile.PropertiesFile;
import com.utils.Logger.Logger;
import com.utils.Config.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Manages dependency injection and application lifecycle for server
 *
 * @author Howard Pearce
 */
public class ServerApplication extends BaseApplication {
    /**
     * Port we will listen for connections on
     */
    private Integer m_port = null;
    /**
     * Manages network communications for us
     */
    private ServerNetworkManager m_networkManager = null;
    /**
     * How long server should wait before re-opening connection
     */
    private Integer m_retryPeriod = null;

    /**
     * Manage state if connection is lost so we can retry to connect
     */
    private boolean m_lostConnection = false;

    /**
     * Constructor to read in service configuration
     *
     * @param configPath filepath to server configuration as String
     * @throws ConfigurationException if configuration cannot be opened
     */
    public ServerApplication(String configPath) throws ConfigurationException {
        super(configPath, "server");
    }

    /**
     * @param args user provided arguments
     */
    public static void main(String[] args) {
        // validate args before we start
        String areArgsValid = areArgumentsValid(args);
        if (areArgsValid.length() > 0) {
            System.out.println(areArgsValid);
            return;
        }
        Logger.logInfo("Initializing Server");
        try {
            ServerApplication application = new ServerApplication(args[0]);
            application.readConfiguration();
            application.initializeServerNetworkManager();
            application.run();
        } catch (ConfigurationException e) {
            System.out.println("Unable to open/read configuration file. Cannot start: " + e.getMessage());
        }
    }

    /**
     * Start the primary execution loop for the ServerApplication
     */
    public void run() {
        while (true) {
            try {
                m_networkManager.run();
            } catch (IOException e) {
                Logger.logError("Connection with client has been lost. " + e.getMessage());
                m_lostConnection = true;
            }
            if (m_lostConnection) {
                m_networkManager.closeConnection();
                Logger.logInfo("Re-opening connection.");
                m_networkManager.waitForConnection();
                m_lostConnection = false;
            }
        }
    }

    /**
     * Write the properties file we received into our configured directory.
     * Normally this would go into its own 'ServerDirectoryManager' class, but only this function would go inside. Refactor to do so if more directory functionality comes up.
     *
     * @param file the PropertiesFile to write to disk
     */
    public void writeFile(PropertiesFile file) {
        Logger.logInfo("Attempting to write file to disk.");
        try {
            if (!FileUtils.doesFileExist(m_directory + "/" + file.getFileName())) {
                FileUtils.createFile(m_directory + "/" + file.getFileName(), file.renderAsFile());
            } else {
                Logger.logError("File with that name already exists. Cannot write. ");
            }
        } catch (IOException e) {
            Logger.logError("Failed to create new properties file: " + e.getMessage());
        }
    }

    /**
     * Generate required resources to create a serverNetworkManager instance
     *
     */
    public void initializeServerNetworkManager() {
        m_networkManager = new ServerNetworkManager(m_port, this, m_retryPeriod);
    }

    /**
     * Read in configuration necessary to start the application.  Does not verify ranges or values for verification.
     *
     * @throws ConfigurationException if configuration cannot be read
     */
    public void readConfiguration() throws ConfigurationException {
        super.readConfiguration(m_configurationManager);
        m_port = m_configurationManager.getConfigItemAsInteger(m_applicationName + ".port");
        m_retryPeriod = m_configurationManager.getConfigItemAsInteger(m_applicationName + ".retryPeriod");
    }

    /**
     * Read in the arguments provided by the user and validate them.
     *
     * @param args the arguments from the main method
     * @return A string that describes what is wrong with the arguments provided.
     */
    public static String areArgumentsValid(String[] args) {
        if (args.length != 1) {
            return "Invalid number of arguments provided. See readme for more info on how to run.";
        }
        return "";
    }
}
