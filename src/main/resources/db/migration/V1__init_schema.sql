-- ============================================================================
-- PHARMACY POS - ENTERPRISE SCHEMA
-- Multi-Branch / Franchise (Multi-Tenant) / Offline-First Sync
-- ============================================================================

-- ==========================================
-- ENUMS
-- ==========================================
CREATE TYPE order_status AS ENUM ('COMPLETED', 'VOIDED', 'REFUNDED', 'PARTIALLY_REFUNDED', 'PENDING_SYNC');
CREATE TYPE payment_method AS ENUM ('CASH', 'KHQR', 'CARD', 'CREDIT', 'BANK_TRANSFER', 'WALLET');
CREATE TYPE movement_type AS ENUM ('PURCHASE_IN', 'SALE_OUT', 'TRANSFER_IN', 'TRANSFER_OUT', 'ADJUSTMENT_IN', 'ADJUSTMENT_OUT', 'RETURN_IN', 'EXPIRED_OUT', 'DAMAGED_OUT');
CREATE TYPE purchase_status AS ENUM ('DRAFT', 'ORDERED', 'PARTIALLY_RECEIVED', 'RECEIVED', 'CANCELLED');
CREATE TYPE transfer_status AS ENUM ('REQUESTED', 'IN_TRANSIT', 'RECEIVED', 'CANCELLED');
CREATE TYPE shift_status AS ENUM ('OPEN', 'CLOSED', 'RECONCILED');
CREATE TYPE sync_status AS ENUM ('PENDING', 'SYNCED', 'CONFLICT', 'FAILED');
CREATE TYPE subscription_plan_status AS ENUM ('TRIAL', 'ACTIVE', 'SUSPENDED', 'CANCELLED');

-- ==========================================
-- 0. TENANT / FRANCHISE MODULE (Multi-Tenant)
-- ==========================================
CREATE TABLE organizations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    slug VARCHAR UNIQUE NOT NULL,
    license_number VARCHAR,
    contact_email VARCHAR,
    contact_phone VARCHAR,
    address VARCHAR,
    logo_url VARCHAR,
    base_currency VARCHAR(3) DEFAULT 'USD',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE subscription_plans (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    plan_name VARCHAR NOT NULL,
    max_branches INT,
    max_users INT,
    status subscription_plan_status DEFAULT 'TRIAL',
    starts_at DATE,
    ends_at DATE,
    created_at TIMESTAMP
);

-- ==========================================
-- 1. BRANCH & DEVICE (Offline Sync) MODULE
-- ==========================================
CREATE TABLE branches (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    code VARCHAR UNIQUE NOT NULL,
    name VARCHAR NOT NULL,
    location VARCHAR,
    phone VARCHAR,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE branch_settings (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT UNIQUE NOT NULL REFERENCES branches(id),
    tax_rate DECIMAL(5,2) DEFAULT 0,
    receipt_header TEXT,
    receipt_footer TEXT,
    allow_negative_stock BOOLEAN DEFAULT false,
    default_payment_method payment_method,
    updated_at TIMESTAMP
);

CREATE TABLE devices (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    device_uuid VARCHAR UNIQUE NOT NULL,
    device_name VARCHAR,
    last_synced_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    registered_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 2. PRODUCT & CATALOG MODULE
-- ==========================================
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    parent_id BIGINT REFERENCES categories(id),
    name VARCHAR NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE active_ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE suppliers (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    name VARCHAR NOT NULL,
    contact_person VARCHAR,
    phone VARCHAR,
    email VARCHAR,
    address VARCHAR,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    sku VARCHAR NOT NULL,
    brand_name VARCHAR NOT NULL,
    generic_name_id BIGINT REFERENCES active_ingredients(id),
    category_id BIGINT REFERENCES categories(id),
    default_supplier_id BIGINT REFERENCES suppliers(id),
    requires_prescription BOOLEAN DEFAULT false,
    is_controlled_substance BOOLEAN DEFAULT false,
    min_stock_alert INT DEFAULT 10,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (organization_id, sku)
);

CREATE TABLE product_units (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    unit_name VARCHAR NOT NULL,
    barcode VARCHAR UNIQUE,
    conversion_factor INT NOT NULL,
    is_base_unit BOOLEAN DEFAULT false,
    cost_price DECIMAL(12,2),
    selling_price DECIMAL(12,2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE drug_interactions (
    id BIGSERIAL PRIMARY KEY,
    ingredient_a_id BIGINT REFERENCES active_ingredients(id),
    ingredient_b_id BIGINT REFERENCES active_ingredients(id),
    severity VARCHAR,
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 3. BATCH, INVENTORY & STOCK MOVEMENT MODULE
-- ==========================================
CREATE TABLE product_batches (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    batch_number VARCHAR NOT NULL,
    mfg_date DATE,
    expiry_date DATE NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (product_id, batch_number)
);

CREATE TABLE branch_inventories (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    batch_id BIGINT NOT NULL REFERENCES product_batches(id),
    quantity_in_base_unit INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP,
    UNIQUE (branch_id, batch_id)
);

CREATE TABLE stock_movements (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    batch_id BIGINT NOT NULL REFERENCES product_batches(id),
    movement_type movement_type NOT NULL,
    quantity_in_base_unit INT NOT NULL,
    reference_table VARCHAR,
    reference_id BIGINT,
    performed_by BIGINT,
    created_at TIMESTAMP
);

CREATE TABLE stock_adjustments (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    batch_id BIGINT NOT NULL REFERENCES product_batches(id),
    quantity_in_base_unit INT NOT NULL,
    reason VARCHAR NOT NULL,
    notes TEXT,
    performed_by BIGINT,
    created_at TIMESTAMP
);

CREATE TABLE stock_transfers (
    id BIGSERIAL PRIMARY KEY,
    transfer_number VARCHAR UNIQUE NOT NULL,
    from_branch_id BIGINT NOT NULL REFERENCES branches(id),
    to_branch_id BIGINT NOT NULL REFERENCES branches(id),
    status transfer_status DEFAULT 'REQUESTED',
    requested_by BIGINT,
    received_by BIGINT,
    requested_at TIMESTAMP,
    received_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE stock_transfer_items (
    id BIGSERIAL PRIMARY KEY,
    stock_transfer_id BIGINT NOT NULL REFERENCES stock_transfers(id),
    batch_id BIGINT NOT NULL REFERENCES product_batches(id),
    quantity_in_base_unit INT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 4. PURCHASING MODULE
-- ==========================================
CREATE TABLE purchase_orders (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    supplier_id BIGINT NOT NULL REFERENCES suppliers(id),
    po_number VARCHAR UNIQUE NOT NULL,
    status purchase_status DEFAULT 'DRAFT',
    total_amount DECIMAL(12,2),
    created_by BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE purchase_order_items (
    id BIGSERIAL PRIMARY KEY,
    purchase_order_id BIGINT NOT NULL REFERENCES purchase_orders(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    unit_id BIGINT NOT NULL REFERENCES product_units(id),
    quantity INT NOT NULL,
    unit_cost DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE goods_receipts (
    id BIGSERIAL PRIMARY KEY,
    purchase_order_id BIGINT NOT NULL REFERENCES purchase_orders(id),
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    received_by BIGINT,
    received_at TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE goods_receipt_items (
    id BIGSERIAL PRIMARY KEY,
    goods_receipt_id BIGINT NOT NULL REFERENCES goods_receipts(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    batch_id BIGINT NOT NULL REFERENCES product_batches(id),
    unit_id BIGINT NOT NULL REFERENCES product_units(id),
    quantity INT NOT NULL,
    unit_cost DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 5. USER, ROLE (RBAC), SHIFT MODULE
-- ==========================================
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT REFERENCES organizations(id),
    name VARCHAR NOT NULL,
    is_system_role BOOLEAN DEFAULT false,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR UNIQUE NOT NULL,
    description VARCHAR,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE role_permissions (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL REFERENCES roles(id),
    permission_id BIGINT NOT NULL REFERENCES permissions(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (role_id, permission_id)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    role_id BIGINT NOT NULL REFERENCES roles(id),
    name VARCHAR NOT NULL,
    username VARCHAR UNIQUE NOT NULL,
    password_hash VARCHAR NOT NULL,
    phone VARCHAR,
    pin_code VARCHAR,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE user_branches (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (user_id, branch_id)
);

CREATE TABLE shifts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    device_id BIGINT REFERENCES devices(id),
    opening_cash DECIMAL(12,2) NOT NULL,
    expected_cash DECIMAL(12,2),
    actual_cash DECIMAL(12,2),
    difference DECIMAL(12,2),
    status shift_status DEFAULT 'OPEN',
    opened_at TIMESTAMP NOT NULL,
    closed_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 6. CUSTOMER & PRESCRIPTION MODULE
-- ==========================================
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    name VARCHAR NOT NULL,
    phone VARCHAR,
    date_of_birth DATE,
    loyalty_points INT DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (organization_id, phone)
);

CREATE TABLE customer_allergies (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    ingredient_id BIGINT NOT NULL REFERENCES active_ingredients(id),
    reaction_notes TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    license_number VARCHAR,
    phone VARCHAR,
    clinic_name VARCHAR,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE prescriptions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    doctor_id BIGINT REFERENCES doctors(id),
    prescription_image_url VARCHAR,
    issued_date DATE,
    is_refillable BOOLEAN DEFAULT false,
    refills_remaining INT DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE prescription_items (
    id BIGSERIAL PRIMARY KEY,
    prescription_id BIGINT NOT NULL REFERENCES prescriptions(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    dosage_instruction TEXT,
    quantity_prescribed INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 7. POS SALES & PAYMENT MODULE
-- ==========================================
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    client_uuid VARCHAR UNIQUE NOT NULL,
    invoice_number VARCHAR UNIQUE NOT NULL,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    branch_id BIGINT NOT NULL REFERENCES branches(id),
    device_id BIGINT REFERENCES devices(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    customer_id BIGINT REFERENCES customers(id),
    shift_id BIGINT REFERENCES shifts(id),
    prescription_id BIGINT REFERENCES prescriptions(id),
    prescription_url VARCHAR,
    subtotal DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) DEFAULT 0,
    tax_amount DECIMAL(12,2) DEFAULT 0,
    grand_total DECIMAL(12,2) NOT NULL,
    status order_status DEFAULT 'COMPLETED',
    sync_status sync_status DEFAULT 'SYNCED',
    created_at_device TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    batch_id BIGINT NOT NULL REFERENCES product_batches(id),
    unit_id BIGINT NOT NULL REFERENCES product_units(id),
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    dosage_instruction TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    payment_method payment_method NOT NULL,
    amount_paid DECIMAL(12,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    exchange_rate_used DECIMAL(10,4) DEFAULT 1,
    transaction_ref VARCHAR,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE order_returns (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    processed_by BIGINT,
    reason TEXT,
    refund_amount DECIMAL(12,2) NOT NULL,
    refund_method payment_method,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE order_return_items (
    id BIGSERIAL PRIMARY KEY,
    order_return_id BIGINT NOT NULL REFERENCES order_returns(id),
    order_item_id BIGINT NOT NULL REFERENCES order_items(id),
    quantity INT NOT NULL,
    restock BOOLEAN DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 8. PROMOTIONS & LOYALTY MODULE
-- ==========================================
CREATE TABLE promotions (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    name VARCHAR NOT NULL,
    discount_type VARCHAR,
    discount_value DECIMAL(12,2),
    starts_at TIMESTAMP,
    ends_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE promotion_usages (
    id BIGSERIAL PRIMARY KEY,
    promotion_id BIGINT NOT NULL REFERENCES promotions(id),
    order_id BIGINT NOT NULL REFERENCES orders(id),
    discount_applied DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE loyalty_transactions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    order_id BIGINT REFERENCES orders(id),
    points_change INT NOT NULL,
    reason VARCHAR,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 9. AUDIT & SYNC MODULE (Offline-First Support)
-- ==========================================
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT REFERENCES organizations(id),
    user_id BIGINT REFERENCES users(id),
    action VARCHAR NOT NULL,
    table_name VARCHAR NOT NULL,
    record_id BIGINT NOT NULL,
    old_value TEXT,
    new_value TEXT,
    created_at TIMESTAMP
);

CREATE TABLE sync_queue (
    id BIGSERIAL PRIMARY KEY,
    device_id BIGINT NOT NULL REFERENCES devices(id),
    table_name VARCHAR NOT NULL,
    record_client_uuid VARCHAR NOT NULL,
    payload_json TEXT NOT NULL,
    status sync_status DEFAULT 'PENDING',
    attempt_count INT DEFAULT 0,
    last_error TEXT,
    created_at TIMESTAMP,
    synced_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ==========================================
-- 10. FINANCE / CURRENCY MODULE
-- ==========================================
CREATE TABLE exchange_rates (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    rate DECIMAL(10,4) NOT NULL,
    effective_date DATE NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (organization_id, from_currency, to_currency, effective_date)
);

-- ==========================================
-- FOREIGN KEY CONSTRAINTS FOR USER REFERENCES
-- ==========================================
ALTER TABLE stock_movements ADD CONSTRAINT fk_stock_movements_performed_by 
    FOREIGN KEY (performed_by) REFERENCES users(id);
ALTER TABLE stock_adjustments ADD CONSTRAINT fk_stock_adjustments_performed_by 
    FOREIGN KEY (performed_by) REFERENCES users(id);
ALTER TABLE stock_transfers ADD CONSTRAINT fk_stock_transfers_requested_by 
    FOREIGN KEY (requested_by) REFERENCES users(id);
ALTER TABLE stock_transfers ADD CONSTRAINT fk_stock_transfers_received_by 
    FOREIGN KEY (received_by) REFERENCES users(id);
ALTER TABLE purchase_orders ADD CONSTRAINT fk_purchase_orders_created_by 
    FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE goods_receipts ADD CONSTRAINT fk_goods_receipts_received_by 
    FOREIGN KEY (received_by) REFERENCES users(id);
ALTER TABLE orders ADD CONSTRAINT fk_orders_user_id 
    FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE order_returns ADD CONSTRAINT fk_order_returns_processed_by 
    FOREIGN KEY (processed_by) REFERENCES users(id);
ALTER TABLE audit_logs ADD CONSTRAINT fk_audit_logs_user_id 
    FOREIGN KEY (user_id) REFERENCES users(id);

-- ==========================================
-- INDEXES
-- ==========================================
CREATE INDEX idx_products_organization ON products(organization_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_supplier ON products(default_supplier_id);
CREATE INDEX idx_branches_organization ON branches(organization_id);
CREATE INDEX idx_users_organization ON users(organization_id);
CREATE INDEX idx_users_role ON users(role_id);
CREATE INDEX idx_orders_organization ON orders(organization_id);
CREATE INDEX idx_orders_branch ON orders(branch_id);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_shift ON orders(shift_id);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_stock_movements_branch ON stock_movements(branch_id);
CREATE INDEX idx_stock_movements_batch ON stock_movements(batch_id);
CREATE INDEX idx_stock_movements_created_at ON stock_movements(created_at);
CREATE INDEX idx_branch_inventories_branch ON branch_inventories(branch_id);
CREATE INDEX idx_branch_inventories_batch ON branch_inventories(batch_id);
CREATE INDEX idx_product_batches_product ON product_batches(product_id);
CREATE INDEX idx_product_batches_expiry ON product_batches(expiry_date);
CREATE INDEX idx_customers_organization ON customers(organization_id);
CREATE INDEX idx_prescriptions_customer ON prescriptions(customer_id);
CREATE INDEX idx_purchase_orders_organization ON purchase_orders(organization_id);
CREATE INDEX idx_purchase_orders_branch ON purchase_orders(branch_id);
CREATE INDEX idx_sync_queue_device ON sync_queue(device_id);
CREATE INDEX idx_sync_queue_status ON sync_queue(status);
CREATE INDEX idx_audit_logs_organization ON audit_logs(organization_id);
CREATE INDEX idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_table ON audit_logs(table_name);
CREATE INDEX idx_loyalty_transactions_customer ON loyalty_transactions(customer_id);
