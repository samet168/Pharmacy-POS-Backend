-- ============================================================================
-- PHARMACY POS - ADD ACTIVE INGREDIENT PERMISSIONS
-- ============================================================================

-- Add active ingredient permissions
INSERT INTO permissions (code, description, created_at, updated_at)
VALUES 
    ('active-ingredients.view', 'Ability to view active ingredients', NOW(), NOW()),
    ('active-ingredients.create', 'Ability to create active ingredients', NOW(), NOW()),
    ('active-ingredients.update', 'Ability to update active ingredients', NOW(), NOW()),
    ('active-ingredients.delete', 'Ability to delete active ingredients', NOW(), NOW())
ON CONFLICT (code) DO NOTHING;

-- Get permission IDs
DECLARE perm_active_ingredients_view BIGINT;
DECLARE perm_active_ingredients_create BIGINT;
DECLARE perm_active_ingredients_update BIGINT;
DECLARE perm_active_ingredients_delete BIGINT;

SELECT id INTO perm_active_ingredients_view FROM permissions WHERE code = 'active-ingredients.view';
SELECT id INTO perm_active_ingredients_create FROM permissions WHERE code = 'active-ingredients.create';
SELECT id INTO perm_active_ingredients_update FROM permissions WHERE code = 'active-ingredients.update';
SELECT id INTO perm_active_ingredients_delete FROM permissions WHERE code = 'active-ingredients.delete';

-- Get Owner role ID
DECLARE owner_role_id BIGINT;
SELECT id INTO owner_role_id FROM roles WHERE name = 'Owner' AND is_system_role = true;

-- Give Owner all new permissions
INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
VALUES 
    (owner_role_id, perm_active_ingredients_view, NOW(), NOW()),
    (owner_role_id, perm_active_ingredients_create, NOW(), NOW()),
    (owner_role_id, perm_active_ingredients_update, NOW(), NOW()),
    (owner_role_id, perm_active_ingredients_delete, NOW(), NOW())
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Get Pharmacist role ID
DECLARE pharmacist_role_id BIGINT;
SELECT id INTO pharmacist_role_id FROM roles WHERE name = 'Pharmacist' AND is_system_role = true;

-- Give Pharmacist view permission
INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
VALUES 
    (pharmacist_role_id, perm_active_ingredients_view, NOW(), NOW())
ON CONFLICT (role_id, permission_id) DO NOTHING;