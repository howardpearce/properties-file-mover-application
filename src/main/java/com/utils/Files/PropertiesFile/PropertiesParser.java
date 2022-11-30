package com.utils.Files.PropertiesFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Reads in a Properties file and parses it as a map
 *
 * @author Howard Pearce
 */
public class PropertiesParser {
    /**
     * file that we will be parsing
     */
    public File m_inputFile;

    /**
     * Create a PropertiesParser object
     *
     * @param inputFile the file that will be parsed
     */
    public PropertiesParser(File inputFile) {
        this.m_inputFile = inputFile;
    }

    /**
     * Parses our internal file into a hashmap. Removes code comments and erroneous lines.
     *
     * @return A hashmap containing the key-value pairs in the properties file.
     * @throws FileNotFoundException if file cannot be opened to parse
     */
    public HashMap<String, String> parse() throws FileNotFoundException {
        HashMap<String, String> parsedFile = new HashMap<>();
        // iterate over every line in the file
        Scanner scan = new Scanner(m_inputFile);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            Property parsedLine = parsePropertiesFileLine(line);
            if (parsedLine != null) {
                parsedFile.put(parsedLine.key(), parsedLine.value());
            }
        }
        scan.close();
        return parsedFile;
    }

    /**
     * Given a line from a properties file, determine what it is
     *
     * @param inputLine A string containing a single line from a properties file
     * @return Enum describing the line that was received
     */
    public PropertyValidity isLineValid(String inputLine) {
        if (inputLine.charAt(0) == '#') {
            return PropertyValidity.OCTOTHORPE_COMMENT;
        } else if (inputLine.charAt(0) == '!') {
            return PropertyValidity.EXCLAMANTION_COMMENT;
        } else if (numberOfOccurrences(':', inputLine) > 1) {
            return PropertyValidity.INVALID;
        } else if (numberOfOccurrences('=', inputLine) > 1) {
            return PropertyValidity.INVALID;
        } else if (numberOfOccurrences(':', inputLine) == 1 && inputLine.contains("=")) {
            return PropertyValidity.INVALID;
        } else if (numberOfOccurrences('=', inputLine) == 1 && inputLine.contains(":")) {
            return PropertyValidity.INVALID;
        } else if (numberOfOccurrences('=', inputLine) == 1) {
            return PropertyValidity.VALID_EQUALS_SIGN;
        } else if (numberOfOccurrences(':', inputLine) == 1) {
            return PropertyValidity.VALID_COLON_SIGN;
        } else {
            return PropertyValidity.UNKNOWN;
        }
    }

    /**
     * Parses a single line from a properties file
     *
     * @param inputLine A single line from a properties file
     * @return Property key-value pair
     */
    private Property parsePropertiesFileLine(String inputLine) {
        // clean up the line
        inputLine = inputLine.trim();
        // check the line for validity to determine how to handle it
        switch (isLineValid(inputLine)) {
            case INVALID, UNKNOWN, EXCLAMANTION_COMMENT, OCTOTHORPE_COMMENT:
                return null;
            case VALID_EQUALS_SIGN: {
                String[] values = inputLine.split("=");
                return new Property(values[0] + "=", values[1]);
            }
            case VALID_COLON_SIGN: {
                String[] values = inputLine.split(":");
                return new Property(values[0] + ":", values[1]);
            }
            default:
                return null;
        }
    }

    /**
     * Counts the number of times a character occurs in a string
     *
     * @param toCount the character that will be counted
     * @param text    the text that will be analyzed
     * @return the number of occurrences of the character
     */
    public int numberOfOccurrences(char toCount, String text) {
        int occurrences = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == toCount) {
                occurrences++;
            }
        }
        return occurrences;
    }

    /**
     * Representation that describes a single line from a Properties File
     */
    private enum PropertyValidity {
        EXCLAMANTION_COMMENT, OCTOTHORPE_COMMENT, INVALID, VALID_EQUALS_SIGN, VALID_COLON_SIGN, UNKNOWN
    }

}
