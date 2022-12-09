package com.server;

import com.utils.Logger.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Manages all connection threads that are currently open
 * @author Howard Pearce
 */
public class ServerConnectionManager {

    /**
     * List of all connections. Could be a map but might be overkill
     */
    private ArrayList<Connection> m_connections = new ArrayList<>();
    /**
     * Count the number of connections we have
     */
    private Integer m_numberOfConnections = 0;
    /**
     * Reference to serverApplication to give to connections to callback
     */
    private ServerApplication m_serverApplication;

    /**
     * Constructs a reference to the server connection manager
     * @param serverApplication callback reference to the server application for the Connections to use
     */
    public ServerConnectionManager(ServerApplication serverApplication) {
        this.m_serverApplication = serverApplication;
    }


    /**
     * Generate dependencies for a new thread that handles a client connection
     *
     * @param clientSocket the client connection we received earlier
     * @throws IOException if we are unable to open an input stream from the client
     */
    public void createNewConnection(Socket clientSocket) throws IOException {
        ObjectInputStream newStream = new ObjectInputStream(clientSocket.getInputStream());
        Logger.logInfo("Creating thread with ID: '" + m_numberOfConnections.toString() + "'");
        Connection newClient = new Connection(m_serverApplication, clientSocket, newStream, m_numberOfConnections.toString());
        newClient.start();
        m_connections.add(newClient);
        m_numberOfConnections += 1;
    }

    /**
     * Clean up and close all remaining connection threads
     */
    public void closeAllConnections() {
        try {
            for (Connection threadToClose : m_connections) {
                Logger.logInfo("Closing connection '" + threadToClose.getConnectionId());
                threadToClose.join();
            }
        } catch (InterruptedException e) {
            Logger.logError("Error occurred while closing connection: " + e.getMessage());
        }
    }

}
