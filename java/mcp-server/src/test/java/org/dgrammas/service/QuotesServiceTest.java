package org.dgrammas.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.dgrammas.model.Quote;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
public class QuotesServiceTest {
    @Inject
    QuotesService service;

    @Test
    void testGetAllQuotes() {
        List<Quote> result = service.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() > 0);

        System.out.println("SIZE="+result.size());
    }

    @Test
    void testGetQuoteByAuthor() {
        Optional<Quote> result = service.getQuoteByAuthor("Seneca");
        assertTrue(result.isPresent());
        System.out.println("SIZE="+result.get().toFormatedText());
    }

    @Test
    void testFindRandomQuote() {
        Optional<Quote> result = service.findRandomQuote();
        assertTrue(result.isPresent());
        System.out.println("SIZE="+result.get().toFormatedText());
    }

    @Test
    void testFindAllAuthors() {
        List<String> result = service.findAllAuthors();
        assertFalse(result.isEmpty());
        System.out.println("All Authors="+result.toString());
    }


    @Test
    void testFindRandomAuthor() {
        Optional<String> result = service.findRandomAuthor();
        assertTrue(result.isPresent());
        System.out.println("RandomAuthor="+result.get());
    }

    @Test
    void testFindRandomQuoteByAuthor() {
        Optional<Quote> result = service.findRandomQuoteByAuthor("Ryan Holiday");
        assertTrue(result.isPresent());
        System.out.println("SIZE="+result.get().toFormatedText());
    }

    @Test
    void testGetTags() {
        String tags = service.getTags();

        assertNotNull(tags);
        System.out.println("SIZE="+tags.length());
    }

    @Test
    void testGetRandomTags() {
        String tags = service.getSampleTags();

        assertNotNull(tags);
        System.out.println(">"+tags);
    }

    @Test
    void testGetRandomQuoteByTag() {
        Optional<Quote> result = service.getRandomQuoteByTag("determination");
        assertTrue(result.isPresent());
        System.out.println("=>"+result.get().toFormatedText());
    }

    @Test
    void testQuoteDatabaseStats() {
        String result = service.getQuoteDatabaseStats();
        assertNotNull(result);
        System.out.println("stats ->"+result);
    }

}
