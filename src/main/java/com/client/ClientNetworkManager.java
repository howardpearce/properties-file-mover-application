package com.client;

import com.utils.Files.PropertiesFile.PropertiesFile;
import com.utils.Logger.Logger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Manages network communications for ClientApplication
 *
 * @author Howard Pearce
 */
public class ClientNetworkManager {
    /**
     * IP Address of the server we are communicating with
     */
    private final String m_serverAddress;
    /**
     * Port number of the server we are communicating with
     */
    private final Integer m_serverPort;
    /**
     * Socket used for communications
     */
    private final Socket m_socket;
    /**
     * Responsible for serializing PropertiesFile object and sending it over socket
     */
    private final ObjectOutputStream m_outputStream;

    /**
     * Creates a ClientNetworkManager object
     *
     * @param serverAddress address injected by clientApplication
     * @param serverPort    port injected by clientApplication
     * @param socket        socket injected by clientApplication
     * @param outputStream  outputStream injected by clientApplication
     */
    public ClientNetworkManager(String serverAddress, Integer serverPort, Socket socket, ObjectOutputStream outputStream) {
        this.m_serverAddress = serverAddress;
        this.m_serverPort = serverPort;
        this.m_socket = socket;
        this.m_outputStream = outputStream;
    }

    /**
     * Sends the provided propertiesFile object to the ServerApplication
     *
     * @param fileToSend file provided by the ClientDirectoryManager
     * @throws IOException if sending fails due to network error
     */
    protected void sendPropertiesFile(PropertiesFile fileToSend) throws IOException {
        m_outputStream.writeObject(fileToSend);
    }

    /**
     * Utility method to attempt to connect to server, will retry if no server is found.
     *
     * @param serverAddress the IP address of the server
     * @param serverPort    the port that the server is listening on
     * @return A new socket with a connection to the server
     */
    public static Socket connectToServer(String serverAddress, Integer serverPort, Integer delay) {
        int attempts = 1;
        while (true) {
            try {
                // attempt to connect to the server
                return new Socket(serverAddress, serverPort);
            } catch (IOException ie) {
                // log that the attempt failed and try again!
                Logger.logError("Unable to connect to server after " + attempts + " attempt(s). Retrying...");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    // do nothing
                }
                attempts++;
            }
        }
    }
}
