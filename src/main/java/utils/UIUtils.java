package utils;

import pojos.TypeSignal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility class providing methods for reading and parsing user input from the console.
 * All methods handle input validation and error cases with appropriate user feedback.
 */
public class UIUtils {

    private static final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    /**
     * Reads an integer value from console input with a prompt message.
     * Continuously prompts until valid integer input is received.
     *
     * @param message The prompt message to display to the user
     * @return The validated integer value entered by the user
     */
    public static int readInt(String message) {

        while (true) {
            try {
                System.out.print(message);
                String input = reader.readLine();
                return Integer.parseInt(input.trim());

            } catch (IOException ex) {
                System.out.println("Error reading input. Try again.");

            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a string value from console input with a prompt message.
     * Returns the trimmed input string if not null.
     *
     * @param message The prompt message to display to the user
     * @return The trimmed string entered by the user
     */
    public static String readString (String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = reader.readLine();
                if (input != null) {
                    return input.trim();
                }
            } catch (IOException ex) {
                System.out.println("Error reading input. Try again.");
            }
        }
    }

    /**
     * Reads a double value from console input with a prompt message.
     * Continuously prompts until valid double input is received.
     *
     * @param message The prompt message to display to the user
     * @return The validated double value entered by the user
     */
    public static double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = reader.readLine();

                return Double.parseDouble(input.trim());

            } catch (IOException ex) {
                System.out.println("Error reading input. Try again.");

            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Parses a string input into a TypeSignal enum value.
     * The input string is trimmed and converted to uppercase before parsing.
     * If parsing fails, a warning message is printed to console.
     *
     * @param raw The raw string input to be parsed into a TypeSignal
     * @return TypeSignal enum value if parsing succeeds, null if input is null or parsing fails
     */
    private TypeSignal parseSignalType(String raw) {
        if (raw == null) return null;

        raw = raw.trim().toUpperCase();

        try {
            return TypeSignal.valueOf(raw);
        } catch (Exception e) {
            System.out.println("WARNING: Cannot parse signal type '" + raw + "'");
            return null;
        }
    }

}
