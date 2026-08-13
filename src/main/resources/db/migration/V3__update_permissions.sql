-- ============================================================================
-- PHARMACY POS - UPDATE PERMISSIONS MIGRATION
-- Add missing user and role permissions
-- ============================================================================

-- Add missing permissions
INSERT INTO permissions (code, description, created_at, updated_at)
VALUES 
    ('user.view', 'Ability to view users', NOW(), NOW()),
    ('user.create', 'Ability to create users', NOW(), NOW()),
    ('user.update', 'Ability to update users', NOW(), NOW()),
    ('user.delete', 'Ability to delete users', NOW(), NOW()),
    ('role.view', 'Ability to view roles', NOW(), NOW()),
    ('role.create', 'Ability to create roles', NOW(), NOW()),
    ('role.update', 'Ability to update roles', NOW(), NOW()),
    ('role.delete', 'Ability to delete roles', NOW(), NOW())
ON CONFLICT (code) DO NOTHING;

-- Get permission IDs
DECLARE perm_user_view BIGINT;
DECLARE perm_user_create BIGINT;
DECLARE perm_user_update BIGINT;
DECLARE perm_user_delete BIGINT;
DECLARE perm_role_view BIGINT;
DECLARE perm_role_create BIGINT;
DECLARE perm_role_update BIGINT;
DECLARE perm_role_delete BIGINT;

SELECT id INTO perm_user_view FROM permissions WHERE code = 'user.view';
SELECT id INTO perm_user_create FROM permissions WHERE code = 'user.create';
SELECT id INTO perm_user_update FROM permissions WHERE code = 'user.update';
SELECT id INTO perm_user_delete FROM permissions WHERE code = 'user.delete';
SELECT id INTO perm_role_view FROM permissions WHERE code = 'role.view';
SELECT id INTO perm_role_create FROM permissions WHERE code = 'role.create';
SELECT id INTO perm_role_update FROM permissions WHERE code = 'role.update';
SELECT id INTO perm_role_delete FROM permissions WHERE code = 'role.delete';

-- Get Owner role ID
DECLARE owner_role_id BIGINT;
SELECT id INTO owner_role_id FROM roles WHERE name = 'Owner' AND is_system_role = true;

-- Give Owner all new permissions
INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
VALUES 
    (owner_role_id, perm_user_view, NOW(), NOW()),
    (owner_role_id, perm_user_create, NOW(), NOW()),
    (owner_role_id, perm_user_update, NOW(), NOW()),
    (owner_role_id, perm_user_delete, NOW(), NOW()),
    (owner_role_id, perm_role_view, NOW(), NOW()),
    (owner_role_id, perm_role_create, NOW(), NOW()),
    (owner_role_id, perm_role_update, NOW(), NOW()),
    (owner_role_id, perm_role_delete, NOW(), NOW())
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Get Manager role ID
DECLARE manager_role_id BIGINT;
SELECT id INTO manager_role_id FROM roles WHERE name = 'Manager' AND is_system_role = true;

-- Give Manager user.view, user.create, user.update, role.view
INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
VALUES 
    (manager_role_id, perm_user_view, NOW(), NOW()),
    (manager_role_id, perm_user_create, NOW(), NOW()),
    (manager_role_id, perm_user_update, NOW(), NOW()),
    (manager_role_id, perm_role_view, NOW(), NOW())
ON CONFLICT (role_id, permission_id) DO NOTHING;