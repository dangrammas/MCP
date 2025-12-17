package org.dgrammas.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.dgrammas.model.Quote;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import static java.lang.System.err;
import static java.lang.System.out;

@ApplicationScoped
public class QuotesRepository {
    private final Map<String, List<Quote>> quotesDatabase = new ConcurrentSkipListMap<>();
    private final Random random = new Random();

    public QuotesRepository() {
      initialize();
    }

    public List<Quote> findAll() {
        return new ArrayList<>(quotesDatabase.values().stream().flatMap(Collection::stream).toList());
    }

    public List<Quote> findAllByAuthor(String author) {
        return new ArrayList<>(quotesDatabase.get(author.trim()).stream().toList());
    }

    public List<String> findAllAuthors() {
        return new ArrayList<>(quotesDatabase.keySet().stream()
                .map(Quote::capitalizeFirst).toList());
    }

    public Optional<String> findRandomAuthor() {
        List<String> authors = new ArrayList<>(quotesDatabase.keySet());
        if (authors.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(authors.get(random.nextInt(authors.size())));
    }
    public Optional<Quote> findRandomQuote() {
        Optional<String> randomAuthor = findRandomAuthor();
        if (randomAuthor.isPresent()) {
            return findRandomQuoteByAuthor(randomAuthor.get());
        }
        return Optional.empty();
    }

    public Optional<Quote> findRandomQuoteByAuthor(String author) {
        String normalizedAuthor = author.toLowerCase().trim();
        List<Quote> authorQuotes = quotesDatabase.get(normalizedAuthor);
        if (authorQuotes.isEmpty()) return Optional.empty();
        int index = random.nextInt(authorQuotes.size());
        Quote updatedQuote = quotesDatabase.get(normalizedAuthor).get(index).withIncrementedAccess();
        authorQuotes.set(index, updatedQuote);
        quotesDatabase.replace(normalizedAuthor, authorQuotes);
        return Optional.of(updatedQuote);
    }

    public Optional<Quote> getRandomQuoteByTag(String tag) {
        List<Quote> quotes = new ArrayList<>(findAll().stream().filter(p -> p.tags().contains(tag)).toList());
        Collections.shuffle(quotes);
        return quotes.stream().findFirst();
    }

    public Set<String> getTagSet() {
        return quotesDatabase.values().stream().flatMap(List::stream)
                .map(Quote::tags)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    public String getTags() {
        return String.join(", ", getTagSet());
    }

    public String getSampleTags() {
            List<String> tags = new ArrayList<>(getTagSet());
            Collections.shuffle(tags);
            return tags.stream().limit(10).collect(Collectors.joining(", "));
    }

    public String getQuoteDatabaseStats() {
        StringBuilder sb = new StringBuilder();

        sb.append("Authors:").append(quotesDatabase.size()).append("\n");
        sb.append("Concepts:").append(getTags()).append("\n");
        List<String> authors = findAllAuthors();
        int index = random.nextInt(authors.size());
        sb.append("Random Author:").append(authors.get(index)).append("\n");
        Optional<Quote> mostAccessed = findAll().stream().max( (Quote a, Quote b) -> Integer.compare(a.accessed(), b.accessed()));
        if (mostAccessed.isPresent()) {
            sb.append("Most accessed quote:").append(mostAccessed.get().toDetailsText()).append("\n");
        }
        return sb.toString();
    }

    private InputStream getQuotesAsStream() {
        var fileName = "stoic_quotes_full.tsv";
        var mcpConfig = Paths.get(fileName);
        if (Files.exists(mcpConfig)) {
            try {
                out.printf("→ Loading quotes from directory: %s%n", mcpConfig.toAbsolutePath());
                return Files.newInputStream(mcpConfig);
            } catch (IOException e) {
                err.printf("⚠︎ Failed to read quotes from directory: %s%n",
                        e.getMessage());
            }
        }

        var resourceStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (resourceStream != null) {
            out.println("→ Loading quotes from resource");
        }
        return resourceStream;
    }



    private void initialize() {
        try (InputStream in = getQuotesAsStream();
             InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by the tab character
                String[] values = line.split("\t");
                List<String> row = new ArrayList<>();
                for (String value : values) {
                    row.add(value.trim()); // Trim whitespace from each value
                }

                String quoteText = row.get(0);
                String author = row.get(1);
                String book = row.get(3);
                String tagString = row.get(3);
                List<String> tags = Arrays.stream(tagString.split(","))
                        .map(String::trim)
                        .toList();

                if (author == null || author.trim().isEmpty()) {
                    author = "unknown";
                }
                List<Quote> authorQuotes = quotesDatabase.computeIfAbsent(author.toLowerCase().trim(),
                        l -> new ArrayList<>());
                authorQuotes.add(new Quote(quoteText,author,book,tags,0));
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

    }
}