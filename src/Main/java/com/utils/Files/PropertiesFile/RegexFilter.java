package com.utils.Files.PropertiesFile;

import java.util.HashMap;
import java.util.Properties;

/**
 * Implementation of BaseFilter that applies a Regex filter on Properties Keys
 *
 * @author Howard Pearce
 */
public class RegexFilter extends BaseFilter {
    /**
     * Regular expression to apply on keys
     */
    private String m_regex = null;

    /**
     * Create an instance of this filter
     */
    public RegexFilter(String regex) {
        this.m_regex = regex;
    }

    /**
     * Filter the provided properties file by only leaving keys that match the loaded regular expression
     *
     * @param fileToFilter the file the filter will be applied on
     */
    public void applyFilter(PropertiesFile fileToFilter) {
        // will replace the old Properties
        HashMap<String, String> filteredProperties = new HashMap<>();
        // iterate over all keys and only include those that match the provided regex
        fileToFilter.getContents().forEach((key, value) -> {
            // match against regex, allow comments still
            if (((String) key).matches(m_regex)) {
                filteredProperties.put(key, value);
            }
        });
        // overwrite old properties
        fileToFilter.setContents(filteredProperties);
    }
}
