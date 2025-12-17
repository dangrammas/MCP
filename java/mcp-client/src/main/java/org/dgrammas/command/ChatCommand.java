package org.dgrammas.command;

import org.dgrammas.client.ChatService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "chat", description = "Initiate a chat session")
public class ChatCommand implements Runnable {

    @Option(names = {"--model"}, description = "Model name")
    private String model;

    @Option(names = {"--provider"}, description = "provider name", defaultValue = "ollama")
    private String provider;

    @Override
    public void run() {

        if (model == null) {
            model = "llama3.2";
        }

        ChatService chatService = new ChatService(provider, model);
        if (chatService.isAvailable()) {
            chatService.startChat();
        } else {
            System.err.println("✗ Chat service is not available. Please check the " + provider + " model connection.");
        }
    }
}
