package org.dgrammas.mcp;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.dgrammas.model.Quote;
import org.dgrammas.model.QuoteResult;
import org.dgrammas.service.QuotesService;

import java.util.List;
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
            return "Unable to retrieve quotes " + e.getMessage();
        }
    }

    @Tool(name = "list_authors", description = "List all authors")
    public String findAllAuthors() {
        try {
            List<String> allAuthors = quotesService.findAllAuthors();
            StringBuilder sb = new StringBuilder();

            for (String name : allAuthors) {
                sb.append("- ").append(name);
            }

            return sb.toString();
        }
        catch (Exception e) {
            return "Unable to retrieve authors " + e.getMessage();
        }
    }
    @Tool(name = "list_quotes_by_author", description = "List all quotes for a particular author")
    public String listAuthorQuotes(@ToolArg(description = "Quote Author") String author)  {
        try {
            if (author == null || author.trim().isEmpty() || author.equalsIgnoreCase("?")) {
                List<String> allAuthors =  quotesService.findAllAuthors();

                return allAuthors.stream()
                        .collect(Collectors.joining(",", "Here is a list of the available authors: [","]"));
            }

            List<Quote> allQuotes = quotesService.findAllByAuthor(author.trim().toLowerCase());
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
            return "Unable to retrieve quotes " + e.getMessage();
        }
    }

    @Tool(name = "get_random_quote", description = "Find a random quote")
    public QuoteResult getRandomQuote()  {
        return quotesService
                .findRandomQuote();
    }

    @Tool(name = "get_random_quote_by_tag", description = "Find a random quote by concept")
    public QuoteResult getRandomQuoteByTag(
            @ToolArg(description = "concept", defaultValue = "stoicism") String tag) {
        return (QuoteResult) quotesService.getRandomQuoteByTag(tag);
    }

    @Tool(name = "list_tags", description = "Find all tags")
    public String getAllTags() {
        try {
            return quotesService.getTags();
        } catch (Exception e) {
            return "Error retrieving tags: "+ e.getMessage();
        }
    }

    @Tool(name = "list_sample_tag", description = "Find some sample concepts")
    public String getSampleTags() {
        try {
            return quotesService.getSampleTags();
        } catch (Exception e) {
            return "Error retrieving sample tags: "+ e.getMessage();
        }
    }

    @Tool(name = "get_quote_stats", description = "Find quote stats")
    public String getQuoteDatabaseStats()  {
        return quotesService.getQuoteDatabaseStats();
    }
}
