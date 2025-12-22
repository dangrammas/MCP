## 🔖 Model Context Protocol (MCP) exercise

This is an exposition of a Java MCP solution.  Specifically, it provides a database of stoic quotes with authors,
source related by concepts.

### Project Structure

- **Server**: Quarkus-based HTTP SSE server with data tools
- **Client**: Interactive CLI client using LangChain4j and Ollama integration

## Project Structure
```
MCP/
└── java
   ├── mcp-client/              # LangChain4j MCP client project
   │    ├── src/main/java/      # Java source code
   │    └── pom.xml    
   └── mcp-server/              # Quarkus MCP server project
       ├── src/main/java/       # Java source code
       └── pom.xml              # Maven dependencies
```

## Setup
This setup assumes Ollama LLM is installed locally.  This requires the following tools installed:

### 1. Java 21
- Install [Microsoft Build of OpenJDK 21](https://microsoft.com/openjdk/) or a Java 21 compatible JDK
- **Minimum version**: Java 21.0.0
- Verify installation: `java --version`
- Ensure JAVA_HOME environment variable is set correctly

### 2. Quarkus CLI
- Install the [Quarkus CLI](https://quarkus.io/guides/cli-tooling)
- Enables quick project creation and development commands
- Verify installation: `quarkus --version`

### 3. Ollama LLM
- In a terminal
- Make sure you have created the mcp.json config in the MCP directory
- Pull the `llama3.2` model for MCP tooling support
```bash
ollama pull llama3.2
```
- Start the Ollama server: `ollama serve`

### 4. Start the MCP server
- In another terminal, start your MCP Server

```bash
cd MCP/java/mcp-server
./mvnw package
./mvnw quarkus:dev
```

### 4. Start the MCP Client
- In yet another terminal, start your MCP Client

```bash
cd MCP/java/mcp-client
./mvnw package
cp ../../mcp.json .
java -jar target/cli-mcp-java-client-jar-with-dependencies.jar -h
```
- This should return immediately after displaying the two available commands: chat and tools
- Run with the chat command to start an LLM chat

```bash
java -jar target/cli-mcp-java-client-jar-with-dependencies.jar chat
```
