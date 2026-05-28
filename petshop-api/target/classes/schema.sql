-- ============================================================
--  Pet Shop E-commerce API — Script DDL (criação manual)
--  Banco: PostgreSQL 14+
--  Uso: execute este script se ddl-auto=none ou para setup inicial
-- ============================================================

-- Categorias de produtos
CREATE TABLE IF NOT EXISTS categories (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL UNIQUE,
    description VARCHAR(500)
);

-- Produtos
CREATE TABLE IF NOT EXISTS products (
    id          BIGSERIAL           PRIMARY KEY,
    name        VARCHAR(200)        NOT NULL,
    description VARCHAR(1000),
    price       NUMERIC(10,2)       NOT NULL,
    stock       INTEGER             NOT NULL CHECK (stock >= 0),
    image_url   VARCHAR(500),
    category_id BIGINT              NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Usuários (autenticação JWT)
CREATE TABLE IF NOT EXISTS users (
    id       BIGSERIAL    PRIMARY KEY,
    name     VARCHAR(200) NOT NULL,
    email    VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL DEFAULT 'USER'
);

-- Clientes
CREATE TABLE IF NOT EXISTS customers (
    id           BIGSERIAL    PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    email        VARCHAR(200) NOT NULL UNIQUE,
    cpf          VARCHAR(14)  NOT NULL UNIQUE,
    phone        VARCHAR(20),
    -- Campos do endereço embutido (Embeddable)
    street       VARCHAR(300),
    number       VARCHAR(20),
    neighborhood VARCHAR(150),
    city         VARCHAR(150),
    state        VARCHAR(2),
    zip_code     VARCHAR(9)
);

-- Pedidos
CREATE TABLE IF NOT EXISTS orders (
    id           BIGSERIAL        PRIMARY KEY,
    customer_id  BIGINT           NOT NULL,
    order_date   TIMESTAMP        NOT NULL DEFAULT NOW(),
    status       VARCHAR(20)      NOT NULL DEFAULT 'PENDING',
    total_amount NUMERIC(10,2)    NOT NULL,
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Itens do pedido
CREATE TABLE IF NOT EXISTS order_items (
    id          BIGSERIAL     PRIMARY KEY,
    order_id    BIGINT        NOT NULL,
    product_id  BIGINT        NOT NULL,
    quantity    INTEGER       NOT NULL CHECK (quantity > 0),
    unit_price  NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_item_order   FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ── Índices ──────────────────────────────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_products_category  ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer    ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_items_order  ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product ON order_items(product_id);
