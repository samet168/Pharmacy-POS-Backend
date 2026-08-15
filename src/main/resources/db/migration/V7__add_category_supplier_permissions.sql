-- ============================================================================
-- PHARMACY POS - ADD CATEGORY AND SUPPLIER PERMISSIONS
-- ============================================================================

-- Add category permissions
INSERT INTO permissions (code, description, created_at, updated_at)
VALUES 
    ('categories.view', 'Ability to view categories', NOW(), NOW()),
    ('categories.create', 'Ability to create categories', NOW(), NOW()),
    ('categories.update', 'Ability to update categories', NOW(), NOW()),
    ('categories.delete', 'Ability to delete categories', NOW(), NOW()),
    ('suppliers.view', 'Ability to view suppliers', NOW(), NOW()),
    ('suppliers.create', 'Ability to create suppliers', NOW(), NOW()),
    ('suppliers.update', 'Ability to update suppliers', NOW(), NOW()),
    ('suppliers.delete', 'Ability to delete suppliers', NOW(), NOW())
ON CONFLICT (code) DO NOTHING;

-- Get permission IDs
DECLARE perm_categories_view BIGINT;
DECLARE perm_categories_create BIGINT;
DECLARE perm_categories_update BIGINT;
DECLARE perm_categories_delete BIGINT;
DECLARE perm_suppliers_view BIGINT;
DECLARE perm_suppliers_create BIGINT;
DECLARE perm_suppliers_update BIGINT;
DECLARE perm_suppliers_delete BIGINT;

SELECT id INTO perm_categories_view FROM permissions WHERE code = 'categories.view';
SELECT id INTO perm_categories_create FROM permissions WHERE code = 'categories.create';
SELECT id INTO perm_categories_update FROM permissions WHERE code = 'categories.update';
SELECT id INTO perm_categories_delete FROM permissions WHERE code = 'categories.delete';
SELECT id INTO perm_suppliers_view FROM permissions WHERE code = 'suppliers.view';
SELECT id INTO perm_suppliers_create FROM permissions WHERE code = 'suppliers.create';
SELECT id INTO perm_suppliers_update FROM permissions WHERE code = 'suppliers.update';
SELECT id INTO perm_suppliers_delete FROM permissions WHERE code = 'suppliers.delete';

-- Get Owner role ID
DECLARE owner_role_id BIGINT;
SELECT id INTO owner_role_id FROM roles WHERE name = 'Owner' AND is_system_role = true;

-- Give Owner all new permissions
INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
VALUES 
    (owner_role_id, perm_categories_view, NOW(), NOW()),
    (owner_role_id, perm_categories_create, NOW(), NOW()),
    (owner_role_id, perm_categories_update, NOW(), NOW()),
    (owner_role_id, perm_categories_delete, NOW(), NOW()),
    (owner_role_id, perm_suppliers_view, NOW(), NOW()),
    (owner_role_id, perm_suppliers_create, NOW(), NOW()),
    (owner_role_id, perm_suppliers_update, NOW(), NOW()),
    (owner_role_id, perm_suppliers_delete, NOW(), NOW())
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Get Manager role ID
DECLARE manager_role_id BIGINT;
SELECT id INTO manager_role_id FROM roles WHERE name = 'Manager' AND is_system_role = true;

-- Give Manager view and create permissions
INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
VALUES 
    (manager_role_id, perm_categories_view, NOW(), NOW()),
    (manager_role_id, perm_categories_create, NOW(), NOW()),
    (manager_role_id, perm_categories_update, NOW(), NOW()),
    (manager_role_id, perm_suppliers_view, NOW(), NOW()),
    (manager_role_id, perm_suppliers_create, NOW(), NOW()),
    (manager_role_id, perm_suppliers_update, NOW(), NOW())
ON CONFLICT (role_id, permission_id) DO NOTHING;