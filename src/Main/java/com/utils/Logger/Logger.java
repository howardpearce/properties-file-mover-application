package com.utils.Logger;

/**
 * Static utility used for printing to console in a controlled mannger
 *
 * @author Howard Pearce
 */
public final class Logger {
    /**
     * Should not be able to instantiate this class since it only offers static functions
     */
    private Logger() {
    }

    /**
     * Log an info message - status information that a user should see.
     *
     * @param msg The message to display
     */
    public static void logInfo(String msg) {
        System.out.println("INFO: " + msg);
    }

    /**
     * Log an error message - unplanned errors in execution have occurred. Informs user of consequences.
     *
     * @param msg The message to display
     */
    public static void logError(String msg) {
        System.out.println("ERROR: " + msg);
    }

    /**
     * Log a debug message - Information for debugging for developers.
     *
     * @param msg The message to display
     */
    public static void logDebug(String msg) {
        System.out.println("DEBUG: " + msg);
    }

}
