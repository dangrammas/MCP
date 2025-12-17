package org.dgrammas.mcp;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class QuotesMcpServerTest {

    @Inject
    QuotesMcpServer mcpServer;

    @Test
    void testListQuotes() {
        String result = mcpServer.getQuotes();

        assertNotNull(result);
        assertTrue(result.contains("Seneca"));
    }
}
