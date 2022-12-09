package com.server;

import com.utils.Files.PropertiesFile.PropertiesFile;
import com.utils.Logger.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Connection extends Thread {
    /**
     * Responsible for deserializing data
     */
    private ObjectInputStream m_input;
    /**
     * Socket representing our connection to the client
     */
    private Socket m_socket;
    /**
     * Callback reference to the server application
     */
    private ServerApplication m_server;
    /**
     * Unique identifier for this thread
     */
    private String m_id;
    /**
     * Number of times this thread has failed to connect
     */
    private int m_connectionsLost = 0;
    /**
     * How many times until we give up on a connection
     */
    private final int MAX_CONNECTION_LOSS = 5;

    /**
     * Create a connection reference
     *
     * @param serverApplication allows callback to the server application
     * @param socket connection with the client
     * @param input data stream coming form the client
     * @param id unique id for this connection
     */
    public Connection(ServerApplication serverApplication, Socket socket, ObjectInputStream input, String id) {
        this.m_input = input;
        this.m_server = serverApplication;
        this.m_socket = socket;
        this.m_id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        Logger.logInfo("Listening for messages on thread '" + m_id + "'");
        while (true) {
            try {
                PropertiesFile file = (PropertiesFile) m_input.readObject();
                System.out.println("Received message on thread '" + m_id + "'");
                m_server.writeFile(file);
            } catch (ClassNotFoundException | IOException e) {
                if(m_connectionsLost > MAX_CONNECTION_LOSS) {
                    Logger.logError("Exceeding " + MAX_CONNECTION_LOSS + " connection errors. Closing connection '" + m_id + "'");
                    break;
                }
                Logger.logError("Error occurred while reading client message in Connection '" + m_id + "'. Skipping. Reason: " + e.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    // do nothing
                }
                m_connectionsLost+=1;
            }
            if (Thread.currentThread().isInterrupted()) {
                Logger.logInfo("Stopping thread '" + m_id + "'");
                break;
            }
        }
        shutdown();
    }

    /**
     * Clean up the resources in this thread before closing it.
     */
    public void shutdown() {
        try {
            if (m_socket != null) {
                m_socket.close();
            }
            if (m_input != null) {
                m_input.close();
            }
        } catch (IOException e) {
            Logger.logError("Error occurred while closing connection '" + m_id + ": " + e.getMessage());
        }
    }

    /**
     * Get the ID of this connection
     */
    public String getConnectionId() {
        return this.m_id;
    }
}
