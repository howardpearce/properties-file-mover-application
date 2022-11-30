package com.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Properties;

import com.utils.Files.FileUtils;
import com.utils.Files.PropertiesFile.BaseFilter;
import com.utils.Files.PropertiesFile.PropertiesFile;
import com.utils.Files.PropertiesFile.PropertiesParser;
import com.utils.Logger.Logger;

/**
 * Responsible for managing properties files for the ClientApplication
 *
 * @author Howard Pearce
 */
public class ClientDirectoryManager {
    /**
     * Represents directory we plan to watch for file changes
     */
    private WatchService m_watchService = null;
    /**
     * Responds to file creation events within a directory
     */
    private WatchKey m_watchKey = null;
    /**
     * Reference to parent to callback when we are ready to send a file
     */
    protected ClientApplication m_clientApplication = null;
    /**
     * the directory we are watching
     */
    private String m_directoryPath = null;
    /**
     * Regex key to filter on
     */
    private BaseFilter m_filter = null;

    /**
     * Construct a ClientDirectoryManager to watch a single directory for changes
     *
     * @param watchService injected WatchService for watching the configured directory
     */
    public ClientDirectoryManager(WatchService watchService, WatchKey watchKey, ClientApplication clientApplication, String directory, BaseFilter filter) {
        this.m_watchService = watchService;
        this.m_watchKey = watchKey;
        this.m_clientApplication = clientApplication;
        this.m_directoryPath = directory;
        this.m_filter = filter;
    }

    /**
     * Indefinitely wait for create events within specified directory
     *
     * @throws InterruptedException if interrupted while waiting for next event
     */
    public void watchForFileChanges() throws InterruptedException, IOException {
        WatchKey currentKey;
        while ((currentKey = m_watchService.take()) != null) {
            for (WatchEvent<?> event : m_watchKey.pollEvents()) {
                String newFileName = event.context().toString();
                Logger.logInfo("Observed new file: " + newFileName);
                String extension = FileUtils.getFileExtension(newFileName);
                if (extension != null && extension.equals("properties")) {
                    // Give a little bit of time for the file to be released by other resources (fixes a bug where file is in use by another program)
                    Thread.sleep(100);
                    // create a PropertiesFile object for the new file
                    String path = m_directoryPath + "/" + event.context().toString();
                    handleWatchEvent(path);
                } else {
                    Logger.logInfo("Ignoring file '" + newFileName + "' due to it not being a properties file.");
                }

            }
            currentKey.reset();
        }
    }

    /**
     * Process the new file raised by the watch event
     *
     * @param path String path to the new file raised by the watch event
     */
    public void handleWatchEvent(String path) {
        try {
            // parse out the file
            PropertiesParser parser = new PropertiesParser(FileUtils.getFile(path));
            HashMap<String, String> fileContents = parser.parse();
            PropertiesFile eventFile = new PropertiesFile(fileContents, path);
            // filter out the keys that don't match regex
            m_filter.applyFilter(eventFile);
            // do not send or delete empty files
            if(eventFile.getContents().size() == 0) {
                Logger.logError("File is empty after filtering. Aborting send.");
                return;
            }
            // send to server
            m_clientApplication.sendPropertiesFileMessage(eventFile);
            // delete the file
            Logger.logInfo("Deleting file.");
            FileUtils.deleteFile(path);
        } catch (IOException e) {
            Logger.logError("Error occurred while monitoring file changes: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
