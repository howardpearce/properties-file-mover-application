package com.server;

import com.utils.Files.PropertiesFile.PropertiesFile;
import com.utils.Logger.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Manages network communications for the ServerApplication
 *
 * @author Howard Pearce
 */
public class ServerNetworkManager {
    /**
     * Port that we will listen on
     */
    private Integer m_port = null;
    /**
     * Socket for receiving data
     */
    private ServerSocket m_serverSocket = null;
    /**
     * socket that holds the connection to the client
     */
    private Socket m_clientSocket = null;
    /**
     * Responsible for deserializing data
     */
    private ObjectInputStream m_inputStream = null;
    /**
     * Callback reference to ServerApplication to call methods from
     */
    private ServerApplication m_serverApplication = null;
    /**
     * How long to wait before re-opening connection
     */
    private Integer m_retryPeriod = null;


    /**
     * Constructs a ServerNetworkManager via injected dependencies
     *
     * @param port              Port number to listen on
     * @param serverSocket      Socket to receive data with
     * @param serverApplication callback reference to the serverapplication to handle message receive events
     */
    public ServerNetworkManager(Integer port, ServerSocket serverSocket, ServerApplication serverApplication, Integer retryPeriod) {
        this.m_port = port;
        this.m_serverSocket = serverSocket;
        this.m_serverApplication = serverApplication;
        this.m_retryPeriod = retryPeriod;
        waitForConnection();
    }

    /**
     * Open up sockets and wait for client to connect. Build inputStream once connection is made.
     */
    public void waitForConnection() {
        boolean connected = false;
        while (!connected) {
            try {
                m_serverSocket = new ServerSocket(m_port);
                Logger.logInfo("Waiting for a connection");
                m_clientSocket = m_serverSocket.accept();
                Logger.logInfo("Got a connection!");
                m_inputStream = new ObjectInputStream(m_clientSocket.getInputStream());
                connected = true;
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
            if (m_inputStream != null) {
                m_inputStream.close();
            }
        } catch (IOException e) {
            Logger.logError("Error occurred while closing server connection: " + e.getMessage());
        }
    }

    /**
     * Primary application logic occurs within, wait for messages from client and act on them.
     *
     * @throws IOException            if network error occurs receiving messages
     * @throws ClassNotFoundException if deserialization into class fails
     */
    public void run() throws IOException {
        Logger.logInfo("Listening for messages...");
        while (true) {
            try {
                PropertiesFile file = (PropertiesFile) m_inputStream.readObject();
                System.out.println("Received message.");
                m_serverApplication.writeFile(file);
            } catch (ClassNotFoundException e) {
                Logger.logError("Error occurred while reading client message. Skipping. Reason: " + e.getMessage());
            }
        }
    }
}
