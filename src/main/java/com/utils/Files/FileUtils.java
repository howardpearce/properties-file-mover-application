package com.utils.Files;

import com.utils.Files.PropertiesFile.Property;
import com.utils.Logger.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Common static function library for reading/writing/deleting files
 *
 * @author Howard Pearce
 */
public final class FileUtils {
    /**
     * Should not be able to instantiate this class since it only offers static functions
     */
    private FileUtils() {
    }

    public static boolean doesFileExist(String filepath) {
        return isFileReadable(filepath);
    }

    /**
     * Create a new file and fill with provided input
     *
     * @param path     the path of the new file we are creating
     * @param contents the contents of the file
     * @throws IOException if the file cannot be written
     */
    public static void createFile(String path, String contents) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(contents);
        fileWriter.close();
    }

    /**
     * Check if the file provided is readable and accessible
     *
     * @param filepath the filepath to check
     * @return if the file is readable, exists, and is not a directory
     */
    public static boolean isFileReadable(String filepath) {
        File fileToCheck = new File(filepath);
        return (fileToCheck.exists() && fileToCheck.canRead() && !fileToCheck.isDirectory());
    }

    /**
     * Validation method called by the other file utils to confirm the file can indeed be accessed
     *
     * @param filepath the filepath to check
     * @throws IOException if the file cannot be read
     */
    private static void validateFileIsReadable(String filepath) throws IOException {
        if (!isFileReadable(filepath)) {
            throw new IOException("Unable to open filepath provided: " + filepath);
        }
    }

    /**
     * Grab a reference to a file
     *
     * @param filepath the file to grab
     * @return a File object that represents the file given
     * @throws IOException if the file cannot be found
     */
    public static File getFile(String filepath) throws IOException {
        validateFileIsReadable(filepath);
        return new File(filepath);
    }

    /**
     * Given a filepath, create a Properties object
     *
     * @param filepath the path to the file
     * @return A new Properties object loaded from the filepath
     * @throws IOException if an error occurs loading the file
     */
    public static Properties getFileAsProperties(String filepath) throws IOException {
        Properties newPropertiesFile = new Properties();
        File propertiesFile = getFile(filepath);
        FileInputStream stream = new FileInputStream(propertiesFile);
        newPropertiesFile.load(stream);
        stream.close();
        return newPropertiesFile;
    }

    /**
     * Deletes a file
     *
     * @param filepath absolute path to the file to delete as a string
     * @throws IOException if deletion fails
     */
    public static void deleteFile(String filepath) throws IOException {
        Files.delete(Path.of(filepath));
    }
}
