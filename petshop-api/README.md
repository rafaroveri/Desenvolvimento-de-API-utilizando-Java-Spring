# Pet Shop API

API REST para gerenciamento de e-commerce de pet shop.

## Stack

- Java 17 + Spring Boot 3.2
- Spring Data JPA + PostgreSQL
- Spring Security + JWT
- SpringDoc OpenAPI (Swagger UI)

## Pré-requisitos

- JDK 17+
- Maven 3.8+
- PostgreSQL 14+

## Configuração

Crie o banco de dados:

```sql
CREATE DATABASE petshop;
```

Configure a senha no `application.properties` (ou via variável de ambiente `DB_PASSWORD`).

## Executar

```bash
mvn spring-boot:run
```

A aplicação sobe na porta **8080** e insere dados de exemplo automaticamente.

## Documentação

Acesse o Swagger UI em `http://localhost:8080/swagger-ui.html`.

Para autenticar, faça login em `POST /api/auth/login` e cole o token no botão **Authorize**.

## Credencial padrão (seed)

```
email: admin@petshop.com
senha: admin123
```
