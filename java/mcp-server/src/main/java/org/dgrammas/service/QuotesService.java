package org.dgrammas.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.dgrammas.model.Quote;
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

    public Optional<Quote> getQuoteByAuthor(String author) {
        String normalAuthor = author.toLowerCase().trim();

        return repository.findRandomQuoteByAuthor(normalAuthor).stream().findFirst();
    }

    public Optional<String> findRandomAuthor() {
        return repository.findRandomAuthor();
    }

    public Optional<Quote> findRandomQuote() {
        return repository.findRandomQuote();
    }

    public Optional<Quote> getRandomQuoteByTag(String tag) {
        return repository.getRandomQuoteByTag(tag);
    }

    public Optional<Quote> findRandomQuoteByAuthor(String author) {
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
