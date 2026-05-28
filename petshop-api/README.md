# 🐾 Pet Shop E-commerce API

API REST completa para gerenciamento de um e-commerce de pet shop, construída com Java 17 + Spring Boot 3.

---

## Modelo de Negócio

A API permite ao proprietário de um pet shop gerenciar seu catálogo de produtos (organizados por categorias), cadastro de clientes e ciclo de vida completo de pedidos — incluindo controle de estoque automatizado, captura de preço no momento da compra e autenticação JWT.

---

## Stack Tecnológica

- **Java 17** + **Spring Boot 3.2**
- **Spring Data JPA** + **Hibernate** (PostgreSQL dialect)
- **Spring Security** + **JWT** (jjwt 0.11)
- **SpringDoc OpenAPI 2.5** (Swagger UI)
- **Bean Validation** (jakarta.validation)
- **Lombok**
- **Maven**

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|-----------|--------------|
| JDK       | 17           |
| Maven     | 3.8+         |
| PostgreSQL| 14+          |

---

## Configuração do Banco de Dados

1. Crie o banco de dados:
```sql
CREATE DATABASE petshop;
```

2. Configure as variáveis de ambiente (ou edite `application.properties`):

```bash
export DB_URL=jdbc:postgresql://localhost:5432/petshop
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
export JWT_SECRET=<base64-256bits>    # opcional — há um default para dev
```

> **Dica:** Para gerar uma chave JWT segura: `openssl rand -base64 32`

O esquema de tabelas é criado automaticamente pelo Hibernate (`ddl-auto=update`).  
O script DDL manual está disponível em `src/main/resources/schema.sql`.

---

## Como Executar

```bash
# 1. Clone o repositório
git clone <url-do-repo>
cd petshop-api

# 2. Compile e execute
mvn spring-boot:run

# Ou gere o JAR:
mvn clean package
java -jar target/petshop-api-1.0.0.jar
```

A aplicação sobe na porta **8080** e insere automaticamente dados de exemplo (5 categorias, 10 produtos, 2 clientes, 1 pedido e 1 usuário admin).

---

## Swagger UI

Acesse a documentação interativa em:
```
http://localhost:8080/swagger-ui.html
```

Para autenticar no Swagger: clique em **Authorize** e cole `Bearer <seu-token>`.

---

## Endpoints

### 🔐 Authentication

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/auth/register` | Registrar usuário | ❌ |
| POST | `/api/auth/login` | Login — retorna JWT | ❌ |

**POST /api/auth/register**
```json
// Request
{ "name": "João Silva", "email": "joao@email.com", "password": "senha123" }

// Response 201
{ "token": "eyJ...", "type": "Bearer", "email": "joao@email.com", "name": "João Silva" }
```

**POST /api/auth/login**
```json
// Request
{ "email": "admin@petshop.com", "password": "admin123" }

// Response 200
{ "token": "eyJ...", "type": "Bearer", "email": "admin@petshop.com", "name": "Admin Pet Shop" }
```

---

### 📁 Categories

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/categories` | Listar todas | ❌ |
| GET | `/api/categories/{id}` | Buscar por ID | ❌ |
| POST | `/api/categories` | Criar | ✅ |
| PUT | `/api/categories/{id}` | Atualizar | ✅ |
| DELETE | `/api/categories/{id}` | Deletar | ✅ |

**POST /api/categories**
```json
// Request
{ "name": "Rações", "description": "Alimentos para pets" }

// Response 201
{ "id": 1, "name": "Rações", "description": "Alimentos para pets" }
```

---

### 🛍️ Products

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/products` | Listar (filtro: `?categoryId=`) | ❌ |
| GET | `/api/products/{id}` | Buscar por ID | ❌ |
| POST | `/api/products` | Criar | ✅ |
| PUT | `/api/products/{id}` | Atualizar | ✅ |
| DELETE | `/api/products/{id}` | Deletar | ✅ |

**POST /api/products**
```json
// Request
{
  "name": "Ração Premium 15kg",
  "description": "Ração para cães adultos",
  "price": 149.90,
  "stock": 50,
  "imageUrl": "https://img.petshop.com/racao.jpg",
  "categoryId": 1
}

// Response 201
{
  "id": 1,
  "name": "Ração Premium 15kg",
  "price": 149.90,
  "stock": 50,
  "category": { "id": 1, "name": "Rações" }
}
```

---

### 👤 Customers

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/customers` | Listar todos | ✅ |
| GET | `/api/customers/{id}` | Buscar por ID | ✅ |
| POST | `/api/customers` | Criar | ✅ |
| PUT | `/api/customers/{id}` | Atualizar | ✅ |
| DELETE | `/api/customers/{id}` | Deletar | ✅ |

**POST /api/customers**
```json
// Request
{
  "name": "Maria Oliveira",
  "email": "maria@email.com",
  "cpf": "987.654.321-00",
  "phone": "(11) 98888-2222",
  "address": {
    "street": "Av. Paulista",
    "number": "1000",
    "neighborhood": "Bela Vista",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01310-100"
  }
}

// Response 201
{ "id": 2, "name": "Maria Oliveira", "email": "maria@email.com", ... }
```

---

### 📦 Orders

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/orders` | Listar todos | ✅ |
| GET | `/api/orders/{id}` | Buscar por ID | ✅ |
| GET | `/api/orders/customer/{customerId}` | Pedidos por cliente | ✅ |
| POST | `/api/orders` | Criar pedido | ✅ |
| PATCH | `/api/orders/{id}/status` | Atualizar status | ✅ |
| DELETE | `/api/orders/{id}` | Deletar pedido | ✅ |

**POST /api/orders**
```json
// Request
{
  "customerId": 1,
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 4, "quantity": 1 }
  ]
}

// Response 201
{
  "id": 1,
  "customerId": 1,
  "customerName": "João da Silva",
  "orderDate": "2025-01-01T10:00:00",
  "status": "PENDING",
  "totalAmount": 349.70,
  "items": [
    { "productId": 1, "productName": "Ração Premium 15kg", "quantity": 2, "unitPrice": 149.90, "subtotal": 299.80 },
    { "productId": 4, "productName": "Bola Kong M", "quantity": 1, "unitPrice": 49.90, "subtotal": 49.90 }
  ]
}
```

**PATCH /api/orders/{id}/status**
```json
// Request
{ "status": "CONFIRMED" }

// Status possíveis: PENDING → CONFIRMED → SHIPPED → DELIVERED | CANCELLED
```

---

## Formato Padrão de Erro

```json
{
  "timestamp": "2025-01-01T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Produto não encontrado com id: 5",
  "path": "/api/products/5"
}
```

Para erros de validação (400), há também o campo `fieldErrors`:
```json
{
  "fieldErrors": {
    "price": "O preço deve ser positivo",
    "email": "Formato de email inválido"
  }
}
```

---

## Dados de Exemplo (Seed)

Ao iniciar, a aplicação insere automaticamente:

| Recurso | Quantidade | Destaque |
|---------|-----------|---------|
| Categorias | 5 | Rações, Brinquedos, Higiene, Medicamentos, Acessórios |
| Produtos | 10 | Preços de R$19,90 a R$199,90 |
| Clientes | 2 | João da Silva, Maria Oliveira |
| Pedidos | 1 | Status CONFIRMED, 2 itens |
| Usuário admin | 1 | admin@petshop.com / admin123 |

---

## Decisões de Design

1. **Address como `@Embeddable`** — Evita tabela separada e JOIN desnecessário para um relacionamento 1:1 simples. Tradeoff: se endereços históricos precisarem ser mantidos no futuro, migrar para entidade separada.

2. **User separado de Customer** — Desacopla autenticação de dados de negócio, permitindo que funcionários também tenham acesso sem serem clientes.

3. **Seed via `CommandLineRunner`** — Mais robusto que `data.sql` para dados que dependem de hashing de senha (BCrypt) e lógica condicional (idempotência).

4. **Queries com `JOIN FETCH`** — Evita N+1 queries ao carregar pedidos com itens e produtos, essencial para performance.

5. **Preço capturado no `OrderItem`** — Garante imutabilidade histórica: alterações de preço não afetam pedidos já realizados.

6. **Restauração de estoque no cancelamento** — Ao fazer PATCH status=CANCELLED ou DELETE no pedido, o estoque é automaticamente devolvido.

7. **GETs de catálogo públicos** — Categorias e produtos em GET são permitidos sem autenticação, viabilizando uma vitrine pública.
