package org.dgrammas.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.dgrammas.model.Quote;
import org.dgrammas.model.QuoteResult;
import org.dgrammas.repository.QuotesRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class QuotesService {

    @Inject
    QuotesRepository repository;

    public List<Quote> findAll() {
        return repository.findAll();
    }

    public List<Quote> findAllByAuthor(String author) {
        return repository.findAllByAuthor(author);
    }

    public List<String> findAllAuthors() {
        return repository.findAllAuthors();
    }

    public QuoteResult getQuoteByAuthor(String author) {
        String normalAuthor = author.toLowerCase().trim();

        return repository.findRandomQuoteByAuthor(normalAuthor);
    }

    public Optional<String> findRandomAuthor() {
        return repository.findRandomAuthor();
    }

    public QuoteResult findRandomQuote() {
        return repository.findRandomQuote();
    }

    public QuoteResult getRandomQuoteByTag(String tag) {
        return repository.getRandomQuoteByTag(tag);
    }

    public QuoteResult findRandomQuoteByAuthor(String author) {
        return repository.findRandomQuoteByAuthor(author);
    }

    public String getTags() {
        return repository.getTags();
    }

    public String getSampleTags() {
        return repository.getSampleTags();
    }

    public String getQuoteDatabaseStats() {
        return repository.getQuoteDatabaseStats();
    }
}
