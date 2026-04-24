# auth-chat-api

## Sobre o projeto

O `auth-chat-api` é uma API backend desenvolvida com Spring Boot para autenticação de usuários e conversa em tempo real.  
O projeto combina endpoints REST para gestão de acesso (login, cadastro e redefinição de senha) com mensageria WebSocket/STOMP para comunicação no chat.

Principais capacidades:

- autenticação e autorização com token JWT;
- criação e consulta de grupos de colaboração;
- envio e recebimento de mensagens em tempo real na sala geral (`/ws`, STOMP);
- consulta de histórico de mensagens da sala geral via HTTP;
- persistência com JPA e PostgreSQL, com versionamento de banco via Flyway.
