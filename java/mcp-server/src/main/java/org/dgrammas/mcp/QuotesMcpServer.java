package org.dgrammas.mcp;

import io.quarkiverse.mcp.server.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.dgrammas.model.Quote;
import org.dgrammas.service.QuotesService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class QuotesMcpServer {
    @Inject
    QuotesService  quotesService;

    @Tool(name = "list_quotes", description = "List all available quotes")
    public String getQuotes() {
        try {
            List<Quote> allQuotes = quotesService.findAll();

            if (allQuotes.isEmpty()) {
                return "No quotes found";
            }

            StringBuilder sb = new StringBuilder();

            for (Quote quote : allQuotes) {
                sb.append("- ").append(quote.toString());
            }

            return sb.toString();
        }
        catch (Exception e) {
            return "Unable to retrieve quotes " + e.getCause();
        }
    }

    @Tool(name = "list_quotes_by_author", description = "List all quotes for a particular author")
    public String listAuthorQuotes(String author)  {
        try {
            if (author == null || author.trim().isEmpty() || author.equalsIgnoreCase("?")) {
                List<String> allAuthors =  quotesService.findAllAuthors();

                return allAuthors.stream()
                        .collect(Collectors.joining(",", "Here is a list of the available authors: [","]"));
            }

            List<Quote> allQuotes = quotesService.findAllByAuthor(author);
            if (allQuotes.isEmpty()) {
                return "No quotes found";
            }

            StringBuilder sb = new StringBuilder();

            for (Quote quote : allQuotes) {
                sb.append("- ").append(quote.toString());
            }

            return sb.toString();
        }
        catch (Exception e) {
            return "Unable to retrieve quotes " + e.getCause();
        }
    }

    @Tool(name = "get_random_quote", description = "Find a random quote")
    public String getRandomQuote()  {
        try {
            Optional<Quote> quote = quotesService.findRandomQuote();
            return quote.map(Quote::toFormatedText).orElse("No quote found");
        }
        catch (Exception e) {
            return "Error retrieving quote: "+ e.getMessage();
        }
    }

    @Tool(name = "get_random_quote_by_tag", description = "Find a random quote by concept")
    public String getRandomQuoteByTag(String tag) {
        try {
            Optional<Quote> quote = quotesService.getRandomQuoteByTag(tag);
            return quote.map(Quote::toFormatedText).orElse("No quote found");
        } catch (Exception e) {
            return "Error retrieving tags: "+ e.getMessage();
        }
    }

    @Tool(name = "list_tags", description = "Find all concepts")
    public String getAllTags() {
        try {
            return quotesService.getTags();
        } catch (Exception e) {
            return "Error retrieving tags: "+ e.getMessage();
        }
    }

    @Tool(name = "list_random_tag", description = "Find some sample concepts")
    public String getSampleTags() {
        try {
            return quotesService.getSampleTags();
        } catch (Exception e) {
            return "Error retrieving sample tags: "+ e.getMessage();
        }
    }

    @Tool(name = "get_quote_stats", description = "Find quote stats")
    public String getQuoteDatabaseStats()  {
        /**
         * Author count
         * Most accessed author
         * Most accessed quotes
         */
        return quotesService.getQuoteDatabaseStats();
    }
}
