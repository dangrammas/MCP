package org.dgrammas.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.dgrammas.model.QuoteResult;
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

        System.out.println("SIZE="+result.size());
    }

    @Test
    void testGetQuoteByAuthor() {
        QuoteResult result = service.getQuoteByAuthor("Seneca");
        System.out.println("SIZE="+result.toFormatedText());
    }

    @Test
    void testFindRandomQuote() {
        QuoteResult result = service.findRandomQuote();
        System.out.println("SIZE="+result.toFormatedText());
    }

    @Test
    void testFindAllAuthors() {
        List<String> result = service.findAllAuthors();
        assertFalse(result.isEmpty());
        System.out.println("All Authors="+result);
    }


    @Test
    void testFindRandomAuthor() {
        Optional<String> result = service.findRandomAuthor();
        assertTrue(result.isPresent());
        System.out.println("RandomAuthor="+result.get());
    }

    @Test
    void testFindRandomQuoteByAuthor() {
        QuoteResult result = service.findRandomQuoteByAuthor("Ryan Holiday");
        System.out.println("SIZE="+result.toFormatedText());
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
        QuoteResult result = service.getRandomQuoteByTag("determination");
        System.out.println("=>"+result.toFormatedText());
    }

    @Test
    void testQuoteDatabaseStats() {
        String result = service.getQuoteDatabaseStats();
        assertNotNull(result);
        System.out.println("stats ->"+result);
    }

}
