package net.paulem.arcana.utils;

/**
 * Utility methods for working with strings.
 */
public class StringUtils {
    /**
     * Capitalizes the first letter of every whitespace-separated word in the given string
     * and lowercases the rest of each word.
     *
     * @param input the string to capitalize, may be {@code null} or empty
     * @return the capitalized string, or {@code input} unchanged if it is {@code null} or empty
     */
    public static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] words = input.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }
}
