## 🔖 Model Context Protocol (MCP) demonstration

This is a presentation of MCP solutions. Before diving in be sure to install a local Ollama LLM instance.

## What is Model Context Protocol (MCP)?

Model Context Protocol is an open protocol for connecting AI models to external tools, data, and services in a structured,
standardized way.  Large Language Models (LLM) require *context*.  MCP provides on-demand context retrieval with streamed
structured IO.  MCP has a standard format allowing models machine-readable access to schema, discovery and independence 
from prompts.  

Some of the problems MCP solves include:

- Tools integration
- Context management
- Vendor independence
- Well managed Security
- Scaling

### 1. Configuration
- Create Configuration in the root of this project and call it mcp.json
```json
{
  "servers": {
    "stoicmcp": {
      "type": "sse",
      "url": "http://localhost:8080/mcp/sse"
    }
  }
}
```

### 2. Ollama LLM 
- Install [Ollama](https://ollama.com/) to run LLM models locally
- Pull the `llama3.2` model for MCP tooling support
```bash
ollama pull llama3.2
```
- Start the Ollama server: `ollama serve`


**Start your Java MCP server**: [Java Setup and Test →](./java/README.md)

## Example Chat Session

```
$ java -jar target/cli-mcp-java-client-jar-with-dependencies.jar chat

════════════════════════════════════
      Java MCP Chat Client
════════════════════════════════════
→ Starting chat with ollama (llama3.2) + MCP Tools

💡 Try asking:

   • 'What authors do you know?'
   • 'Tell me quote'

Type 'exit', 'quit', or 'bye' to end the conversation
───────────────────────────────------------------────
>

> Tell me about the quote generator.
AI: What a fascinating dataset!

After analyzing the text, I noticed that:

1. **Author diversity**: The authors mentioned are quite diverse, with no single author dominating the list.
2. **Topic breadth**: The topics covered range from inspirational and motivational to philosophical and self-help, indicating a broad interest in personal growth and development.
3. **Tone and language**: The tone is generally positive and uplifting, with a focus on encouraging readers to adopt a more mindful and Stoic approach to life.
4. **Inspirational quotes**: Many quotes are centered around themes of resilience, adaptability, and the importance of living in the present moment.

The most accessed quote, from Alexander Zenon, highlights the need for mindfulness and connection with nature in today's fast-paced world.

If I were to analyze this dataset further, I'd like to explore:

1. **Author clusters**: Grouping authors by similar themes or styles to identify patterns and trends.
2. **Quote analysis**: Breaking down quotes into sentiment analysis, identifying common phrases or themes, and exploring the relationships between quotes and their respective authors.
3. **Book genre identification**: Classifying books based on genres (e.g., self-help, philosophy, inspirational) and analyzing author contributions to each genre.

Please let me know if you'd like me to explore any of these topics further!



```