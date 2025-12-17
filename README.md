## 🔖 Model Context Protocol (MCP) demonstration

This is a presentation of MCP solutions. Before diving in be sure to install a local Ollama LLM instance.


### 1. Configuration
- Create Configuration in the root of this project
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



