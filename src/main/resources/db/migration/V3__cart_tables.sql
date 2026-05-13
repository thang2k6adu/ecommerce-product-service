-- Shopping cart module (migrated from cart-service schema, stored in product DB)

CREATE TABLE carts (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    session_id VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_carts_user_status ON carts(user_id, status);
CREATE INDEX idx_carts_session_status ON carts(session_id, status);

CREATE TABLE cart_items (
    id UUID PRIMARY KEY,
    cart_id UUID NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
    product_id VARCHAR(36) NOT NULL,
    product_variant_id VARCHAR(36),
    quantity INTEGER NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    product_name VARCHAR(500),
    product_image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cart_items_cart ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product ON cart_items(product_id);

COMMENT ON TABLE carts IS 'Shopping carts for authenticated users and guest sessions';
COMMENT ON TABLE cart_items IS 'Line items with denormalized product snapshot fields';
