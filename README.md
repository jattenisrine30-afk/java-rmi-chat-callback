# - Java RMI Chat with Callback

A distributed chat application built with Java RMI (Remote Method Invocation) using the **Callback pattern**.  
The server broadcasts messages to all connected clients or sends private messages to a specific client.

## Architecture

- **Star topology** — Chat server at the center, clients connect to it
- **Callback mechanism** — Clients are also remote objects (the server calls back the clients to deliver messages)

## Projects

| Project | Role |
|---|---|
| `ChatServer` | RMI server — manages connections and message routing |
| `ChatClient` | RMI client — connects, sends and receives messages |

## Features

- Connect with an ID and a username (pseudonym)
- Send a message to **all connected clients**
- Send a **private message** to a specific client
- Disconnect and notify all other clients

## How to Run

### 1. Start the Server
```bash
cd ChatServer/src
java application.ChatServer
```

### 2. Start one or more Clients
```bash
cd ChatClient/src
java application.ClientChat
```

> Always start the server before the clients.

## Technologies

- Java RMI
- RMI Callback pattern
- Java 8+
