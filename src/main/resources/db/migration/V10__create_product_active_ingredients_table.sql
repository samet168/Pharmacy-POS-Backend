-- Create product_active_ingredients join table
CREATE TABLE IF NOT EXISTS product_active_ingredients (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    active_ingredient_id BIGINT NOT NULL REFERENCES active_ingredients(id),
    strength VARCHAR,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE(product_id, active_ingredient_id)
);