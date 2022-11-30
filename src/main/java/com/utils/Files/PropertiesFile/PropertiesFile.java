package com.utils.Files.PropertiesFile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Container for properties file information
 *
 * @author Howard Pearce
 */
public class PropertiesFile implements Serializable {
    /**
     * Contains file contents as a string
     */
    private HashMap<String, String> m_contents;

    /**
     * String representation of file path
     */
    private String m_filepath;

    /**
     * Returns reference to a new PropertiesFile object
     *
     * @param contents The entire contents of the file as a string
     * @param filepath the associated file object that the contents came from
     */
    public PropertiesFile(HashMap<String, String> contents, String filepath) {
        this.m_contents = contents;
        this.m_filepath = filepath;
    }

    /**
     * Access contents of Properties File
     *
     * @return Contents of properties file encoded as a string
     */
    public Map<String, String> getContents() {
        return m_contents;
    }

    /**
     * Set file contents
     *
     * @param contents new file contents
     */
    public void setContents(HashMap<String, String> contents) {
        this.m_contents = contents;
    }

    /**
     * get File path of Properties File
     *
     * @return String representation of properties file path location on system
     */
    public String getfilepath() {
        return m_filepath;
    }

    /**
     * update File path of Properties File
     */
    public void setFilepath(String filepath) {
        this.m_filepath = filepath;
    }

    /**
     * Get the filename without the path for this file
     *
     * @return the name of the properties file
     */
    public String getFileName() {
        // check if path is delimited using '\' or '/'
        return m_filepath.substring(m_filepath.lastIndexOf("/") + 1);
    }

    /**
     * Returns a String representation of the file so that it can be re-built
     *
     * @return String with all contents in the file
     */
    public String renderAsFile() {
        StringBuilder renderedFile = new StringBuilder();
        // iterate over properties and put one on each line
        m_contents.forEach((key, value) -> {
            renderedFile.append((String) key + value + "\n");
        });
        return renderedFile.toString();
    }
}
