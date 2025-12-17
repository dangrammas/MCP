package org.dgrammas.model;

import io.smallrye.common.constraint.NotNull;

import java.util.List;

public record Quote(
    String quote,
    String author,
    String book,
    List<String> tags,
    Integer accessed
) {

    public @NotNull Quote withIncrementedAccess() {
        return new Quote(
        this.quote,
        this.author,
        this.book,
        this.tags,
        this.accessed +1
        );
    }

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

    public String getFormatedAuthor() {
        return capitalizeFirst(author);
    }

    public String getConcepts()  {
        return
               "concepts=" + tags +
                '.';
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
                ", accessed=" + accessed +
                '}';
    }
}
