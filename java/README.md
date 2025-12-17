## 🔖 Model Context Protocol (MCP) exercise

This is an exposition of a Java MCP solution.  Specifically, it provides a database of stoic quotes with authors,
source related by concepts.

### Project Structure

- **Server**: Quarkus-based HTTP SSE server with data tools
- **Client**: Interactive CLI client using LangChain4j and Ollama integration

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
