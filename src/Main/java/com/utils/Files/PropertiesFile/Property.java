package com.utils.Files.PropertiesFile;

/**
 * Represents a single key-value pair in a properties file
 *
 * @param key   the name of the property
 * @param value the value that is assigned to the name
 */
public record Property(String key, String value) {
}
