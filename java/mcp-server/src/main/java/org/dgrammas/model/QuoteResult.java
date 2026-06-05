package org.dgrammas.model;

import java.util.List;

public record QuoteResult(
        String author,
        String quote,
        List<String> tags,
        long accessCount) {


    public static String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c) || c == '_') {
                first = true;
                result.append(' ');
            } else {
                if (first) {
                    result.append(Character.toUpperCase(c));
                    first = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    public String toFormatedText() {
        return "'" + quote + '\'' +
                ", author='" + capitalizeFirst(author) + '\'' +
                '.';
    }

    public String toDetailsText() {
        return "Quote{" +
                "quote='" + quote + '\'' +
                ", author='" + capitalizeFirst(author) + '\'' +
                ", book='" + capitalizeFirst(author) + '\'' +
                ", concepts=" + tags +
                ", accessed=" + accessCount +
                '}';
    }
}




