package org.dgrammas.client;


import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;

import java.time.Duration;
import java.util.Scanner;
import java.util.UUID;

import static java.lang.System.out;

public class ChatService {
    private static final String USER = "USER-" + UUID.randomUUID();

    private final String model;
    private final String provider;
    private ChatModel chatModel;
    private final ToolsService toolsService = new ToolsService();

    private final Assistant assistant;

    public ChatService(String provider, String model) {
        this.provider = provider;
        this.model = model;

        if (provider.equals("ollama")) {
            chatModel = OllamaChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(model)
                    .timeout(Duration.ofSeconds(20))
                    .temperature(0.7)
                    .build();
        } else {
            out.println("⚠︎ Unsupported provider: " + provider);
            chatModel = null;
        }

        var chatMemoryStore = new InMemoryChatMemoryStore();

        assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .toolProvider(toolsService.getToolProvider())
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .maxMessages(20)
                        .chatMemoryStore(chatMemoryStore)
                        .id(memoryId)
                        .build())
                .build();

        // Initialize
        try {
            assistant.query("health-check", "Say YES if you are ready to chat");
        } catch (RuntimeException e) {
            out.println("⚠︎ Failed to connect to " + provider + " model (" + model + "): " + e.getMessage());
            chatModel = null;
            return;
        }

        out.printf("Starting chat with %s (%s)!\n", provider, model);
    }

    public boolean isAvailable() {
        return chatModel != null && assistant != null;
    }

    public void startChat() {
        out.printf("""
                ════════════════════════════════════
                      Java MCP Chat Client
                ════════════════════════════════════
                → Starting chat with %s (%s) + MCP Tools

                💡 Try asking:

                   • 'Tell me quote'
                   • 'What philosophers do you know?'

                Type 'exit', 'quit', or 'bye' to end the conversation
                ───────────────────────────────------------------────
                """, provider, model);

        try (var scanner = new Scanner(System.in)) {
            while (true) {
                out.print("> ");
                var input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    continue;
                }
                switch (input.toLowerCase()) {
                    case "exit", "quit", "bye" -> {
                        out.println("♑︎ Goodbye!");
                        toolsService.shutdown();
                        return;
                    }
                }

                var response = assistant.query(USER, input);
                out.printf("AI: %s\n", response);
            }
        }
    }
}
