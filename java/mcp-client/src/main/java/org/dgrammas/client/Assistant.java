package org.dgrammas.client;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
public interface Assistant {

    @SystemMessage("""
     " You are an AI assistant that can help with question on philosophy, wisdom, love, happiness and stoicism"
     """
    )
    String query(@MemoryId String memoryId, @UserMessage String message);
}
