package org.dgrammas.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.err;
import static java.lang.System.out;

public class ToolsService {
    private McpToolProvider toolProvider;
    private final List<McpClient> mcpClientList = new ArrayList<>();

    public ToolsService() {
        out.println("→ Initializing MCP tools...");
        try {
            registerMCPServers();
            initializeToolProvider();
            out.println("✓ MCP tools initialized successfully");
        } catch (Exception e) {
            err.printf("⚠︎ Failed to initialize MCP tools: %s%n", e.getMessage());
        }
    }

    public McpToolProvider getToolProvider() {
        return toolProvider;
    }

    private InputStream getConfigAsStream() {
        var mcpConfig = Paths.get("mcp.json");
        if (Files.exists(mcpConfig)) {
            try {
                out.printf("→ Loading mcp.json from directory: %s%n", mcpConfig.toAbsolutePath());
                return Files.newInputStream(mcpConfig);
            } catch (IOException e) {
                err.printf("⚠︎ Failed to read mcp.json from directory: %s%n",
                        e.getMessage());
            }
        }

        var resourceStream = getClass().getClassLoader().getResourceAsStream("mcp.json");
        if (resourceStream != null) {
            out.println("→ Loading mcp.json from resource");
        }
        return resourceStream;
    }

    private void initializeToolProvider() {
        if (mcpClientList.isEmpty()) {
            out.println("⚠ No MCP clients registered, tool provider will be empty");
            return;
        }

        toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClientList.toArray(new McpClient[mcpClientList.size()]))
                .build();

        out.printf("✓ Tool provider initialized with %d MCP client(s)%n",
                mcpClientList.size());
    }

    private void registerMCPServers() {
        var inputStream = getConfigAsStream();

        if (inputStream == null) {
            out.println("⚠ mcp.json configuration file not found in working directory or resources");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try (var is = inputStream) {
            var config = objectMapper.readTree(is);
            var servers = config.get("servers");

            if (servers == null || !servers.isObject()) {
                out.println("⚠ No servers configuration found in mcp.json");
                return;
            }

            servers.fieldNames().forEachRemaining(serverName -> {
                registerServer(serverName, servers.get(serverName));
            });

            out.printf("✓ MCP server registration completed. %d server(s) registered%n",
                    mcpClientList.size());
        } catch (IOException e) {
            err.printf("✗ Failed to read mcp.json configuration: %s%n", e.getMessage());
        }
    }

    private void registerServer(String serverName, JsonNode mcpConfigNode) {
        try {
            var url = mcpConfigNode.get("url").asText();
            var type = mcpConfigNode.get("type").asText();

            out.printf("  URL: %s, Type: %s%n", url, type);

            if (!"sse".equals(type)) {
                err.printf("✗ Unsupported transport type '%s' for server: %s%n", type, serverName);
                return;
            }

            out.printf("→ Registering MCP server: %s with URL: %s%n", serverName, url);

            // Configure SSE transport with longer timeouts to prevent connection issues
            var mcpTransport = new HttpMcpTransport.Builder()
                    .sseUrl(url)
                    .timeout(Duration.ofSeconds(60)) // Increased timeout for SSE connections
                    .logRequests(false)
                    .logResponses(false)
                    .build();

            var mcpClient = new DefaultMcpClient.Builder()
                    .key(serverName)
                    .transport(mcpTransport)
                    .build();

            mcpClientList.add(mcpClient);
            out.printf("✓ Successfully registered MCP server: %s%n", serverName);
        } catch (RuntimeException e) {
            err.printf("✗ Failed to register MCP server: %s - %s%n", serverName, e.getMessage());
        }
    }

    public List<ToolSpecification> getAvailableTools() {
        var availableTools = new ArrayList<ToolSpecification>();

        for (var client : mcpClientList) {
            try {
                var clientTools = client.listTools();
                availableTools.addAll(clientTools);
            } catch (Exception e) {
                err.printf("⚠ Failed to get tools from MCP server: %s - %s%n", client.key(), e.getMessage());
                // Don't print full stack trace for common connection issues
                if (!(e.getCause() instanceof java.net.SocketTimeoutException)) {
                    err.printf("  Full error details: %s%n", e.toString());
                }
            }
        }

        out.printf("→ Retrieved %d total tool(s) from %d MCP server(s)%n",
                availableTools.size(), mcpClientList.size());
        return availableTools;
    }

    public void shutdown() {
        out.println("→ Shutting down MCP tools service...");
        mcpClientList.forEach(t -> {
            try {
                t.close();
            } catch (Exception e) {
                err.printf("✗ Failed to close MCP client %s: %s%n", t.key(), e.getMessage());
            } finally {
                out.printf("✓ MCP client %s closed%n", t.key());
            }
        });
        out.println("✓ All MCP clients closed");
    }
}
