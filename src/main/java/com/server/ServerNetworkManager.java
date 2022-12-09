package com.server;

import com.utils.Files.PropertiesFile.PropertiesFile;
import com.utils.Logger.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Manages network communications for the ServerApplication
 *
 * @author Howard Pearce
 */
public class ServerNetworkManager {
    /**
     * Port that we will listen on
     */
    private Integer m_port;
    /**
     * Socket for receiving data
     */
    private ServerSocket m_serverSocket = null;
    /**
     * Callback reference to ServerApplication to call methods from
     */
    private final ServerApplication m_serverApplication;
    /**
     * How long to wait before re-opening connection
     */
    private final Integer m_retryPeriod;

    private ServerConnectionManager m_connectionManager;

    /**
     * Constructs a ServerNetworkManager via injected dependencies
     *
     * @param port              Port number to listen on
     * @param serverApplication callback reference to the serverapplication to handle message receive events
     */
    public ServerNetworkManager(Integer port, ServerApplication serverApplication, Integer retryPeriod) {
        this.m_port = port;
        this.m_serverApplication = serverApplication;
        this.m_retryPeriod = retryPeriod;
        this.m_connectionManager = new ServerConnectionManager(serverApplication);
        waitForConnection();
    }

    /**
     * Open up sockets and wait for client to connect. Build inputStream once connection is made.
     */
    public void waitForConnection() {
        while (true) {
            try {
                m_serverSocket = new ServerSocket(m_port);
                Logger.logInfo("Waiting for a connection on port " + m_port);
                Socket clientSocket = m_serverSocket.accept();
                Logger.logInfo("Got a connection!");
                m_connectionManager.createNewConnection(clientSocket);
                // close connection to allow a new one in
                m_serverSocket.close();
            } catch (IOException e) {
                if (e.getMessage().contains("bind")) {
                    Logger.logError("Port " + m_port + " is already in use. Do you have another server running?");
                } else {
                    Logger.logError("Error ocurred while waiting for client to connect: " + e.getMessage());
                }
                try {
                    Thread.sleep(m_retryPeriod);
                } catch (InterruptedException ie) {
                    // do nothing
                }
            }
        }
    }

    /**
     * Close old sockets in order to open new ones
     */
    public void closeConnection() {
        Logger.logInfo("Closing old server connections.");
        try {
            if (m_serverSocket != null) {
                m_serverSocket.close();
            }
            m_connectionManager.closeAllConnections();
        } catch (IOException e) {
            Logger.logError("Error occurred while closing server connection: " + e.getMessage());
        }
    }
}
