package org.dgrammas;

import org.dgrammas.command.ChatCommand;
import org.dgrammas.command.ToolsCommand;
import picocli.CommandLine;

import java.io.InputStream;
import java.util.logging.LogManager;

@CommandLine.Command(name = "mcp-java-client",
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        description = "console app to chat with AI+MCP servers using Ollama Local llama3.2 model",
        subcommands = {ChatCommand.class, ToolsCommand.class })
public class McpClientApplication {
    public static void main(String[] args) {
        initialize();
        int exitCode = new CommandLine(new McpClientApplication()).execute(args);
        System.exit(exitCode);
    }

    private static void initialize() {
        try (InputStream stream = McpClientApplication.class.getClassLoader()
                .getResourceAsStream("logging.properties")) {
            if (stream != null) {
                LogManager.getLogManager().readConfiguration(stream);
            }
        } catch (Exception e) {
            // Fallback to default logging if properties file fails to load
            System.err.println("Warning: Could not load logging configuration: " + e.getMessage());
        }
    }
}
