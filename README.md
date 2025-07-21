# FoodHub - Restaurant Management API
## Developer: Gustavo Arantes

Uma API RESTful robusta para simular o sistema de pedidos e gerenciamento de um restaurante, incorporando uma arquitetura moderna com microserviços, mensageria, cache e um pipeline de CI/CD completo.

---
## Estrutura do Projeto
<pre>
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── gustavooarantes
    │   │           └── foodhub
    │   │               ├── config
    │   │               ├── consumer
    │   │               ├── controller
    │   │               ├── domain
    │   │               │   └── enums
    │   │               ├── dto
    │   │               ├── repository
    │   │               ├── security
    │   │               └── service
    │   │                   └── notificacao
    │   └── resources
    └── test
        └── java
            └── com
                └── gustavooarantes
                    └── foodhub
                        ├── controller
                        └── service
</pre>
---

## Features

-   **Gerenciamento de Usuários e Segurança (RBAC)**:
    -   Controle de acesso baseado em papéis (`ROLE_ADMIN`, `ROLE_CHEF`, `ROLE_CLIENT`).
    -   Autenticação segura via **JWT (JSON Web Tokens)** com endpoints para registro e login.
-   **Cardápio Dinâmico**: Operações CRUD completas para gerenciar os produtos do cardápio.
-   **Sistema de Pedidos**:
    -   Clientes autenticados podem criar pedidos complexos com múltiplos itens.
    -   Endpoints para administradores e clientes visualizarem e gerenciarem o histórico de pedidos.
    -   Admins/Chefs podem atualizar o status de um pedido (ex: "Em Preparo", "Pronto").
-   **Processamento Assíncrono com RabbitMQ**:
    -   Pedidos recém-criados são enviados para uma fila para **processamento de pagamento simulado**, desacoplando a resposta da API.
    -   Atualizações de status disparam eventos para uma fila de **notificações**, informando os clientes de forma assíncrona.
-   **Cache de Performance com Redis**:
    -   Consultas ao cardápio (`/api/produtos`) são cacheadas para reduzir a carga no banco de dados e aumentar drasticamente a velocidade de resposta.
-   **Padrão de Projeto (Factory)**:
    -   O sistema de notificações utiliza o **Factory Pattern** para criar diferentes tipos de "notificadores" (`Email`, `SMS`), tornando o código mais flexível e extensível.
-   **Documentação da API**: Documentação interativa e automática gerada com **Swagger (OpenAPI)**.
-   **Testes Automatizados**: Cobertura de testes unitários (com Mockito) para a lógica de negócio e testes de integração para os controllers.
-   **CI/CD Completo**:
    -   **Dockerfile** com build multi-stage para criar uma imagem otimizada da aplicação.
    -   Manifestos **Kubernetes** para orquestrar a implantação de toda a arquitetura de serviços.
    -   **Jenkinsfile** que define um pipeline de CI/CD para automatizar o build, teste, e deploy a cada push no repositório.

---

## Tech Stack

-   **Backend**: Java 17, Spring Boot 3
-   **Persistência de Dados**: Spring Data JPA, PostgreSQL
-   **Segurança**: Spring Security, JSON Web Token (JWT)
-   **Mensageria**: RabbitMQ
-   **Caching**: Redis
-   **Containerização**: Docker
-   **Orquestração**: Kubernetes (Minikube)
-   **CI/CD**: Jenkins
-   **API Documentation**: SpringDoc (Swagger/OpenAPI)
-   **Testes**: JUnit 5 & Mockito
-   **Build**: Maven
-   **Padrões de Projeto**: Factory

---

## Visão Geral dos Endpoints da API

### Autenticação (`/api/auth`)

-   `POST /register`: Registra um novo usuário (`ROLE_CLIENT`).
-   `POST /login`: Autentica um usuário e retorna um token JWT.

### Produtos (`/api/produtos`)

-   `GET /`: Lista todos os produtos (Público).
-   `GET /{id}`: Busca um produto por ID (Público).
-   `POST /`: Cria um novo produto (Requer `ROLE_ADMIN`).
-   `PUT /{id}`: Atualiza um produto existente (Requer `ROLE_ADMIN`).
-   `DELETE /{id}`: Remove um produto (Requer `ROLE_ADMIN`).

### Pedidos (`/api/pedidos`)

-   `POST /`: Cliente cria um novo pedido (Requer `ROLE_CLIENT`).
-   `GET /`: Lista todos os pedidos no sistema (Requer `ROLE_ADMIN` ou `ROLE_CHEF`).
-   `GET /meus-pedidos`: Lista os pedidos do cliente autenticado (Requer `ROLE_CLIENT`).
-   `PATCH /{id}/status`: Atualiza o status de um pedido (Requer `ROLE_ADMIN` ou `ROLE_CHEF`).
