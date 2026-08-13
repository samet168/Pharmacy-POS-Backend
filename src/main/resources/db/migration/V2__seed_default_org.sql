-- ============================================================================
-- PHARMACY POS - SEED DATA MIGRATION
-- Default organization, branch, roles, and permissions
-- ============================================================================

-- Only run if no organizations exist yet
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM organizations) THEN
        
        -- 1. Default Organization
        INSERT INTO organizations (name, slug, base_currency, is_active, created_at, updated_at)
        VALUES ('Default Pharmacy Org', 'default-org', 'USD', true, NOW(), NOW());
        
        -- Get the organization ID (should be 1)
        DECLARE org_id BIGINT;
        SELECT id INTO org_id FROM organizations WHERE slug = 'default-org';
        
        -- 2. Default Branch
        INSERT INTO branches (organization_id, code, name, is_active, created_at, updated_at)
        VALUES (org_id, 'MAIN-01', 'Main Branch', true, NOW(), NOW());
        
        -- Get the branch ID (should be 1)
        DECLARE branch_id BIGINT;
        SELECT id INTO branch_id FROM branches WHERE code = 'MAIN-01';
        
        -- 3. Default Branch Settings
        INSERT INTO branch_settings (branch_id, tax_rate, allow_negative_stock, updated_at)
        VALUES (branch_id, 0, false, NOW());
        
        -- 4. System Roles (organization_id = NULL for system roles)
        INSERT INTO roles (organization_id, name, is_system_role, created_at, updated_at)
        VALUES 
            (NULL, 'Owner', true, NOW(), NOW()),
            (NULL, 'Manager', true, NOW(), NOW()),
            (NULL, 'Pharmacist', true, NOW(), NOW()),
            (NULL, 'Cashier', true, NOW(), NOW());
        
        -- Get role IDs
        DECLARE owner_role_id BIGINT;
        DECLARE manager_role_id BIGINT;
        DECLARE pharmacist_role_id BIGINT;
        DECLARE cashier_role_id BIGINT;
        
        SELECT id INTO owner_role_id FROM roles WHERE name = 'Owner' AND is_system_role = true;
        SELECT id INTO manager_role_id FROM roles WHERE name = 'Manager' AND is_system_role = true;
        SELECT id INTO pharmacist_role_id FROM roles WHERE name = 'Pharmacist' AND is_system_role = true;
        SELECT id INTO cashier_role_id FROM roles WHERE name = 'Cashier' AND is_system_role = true;
        
        -- 5. Default Permissions (idempotent using ON CONFLICT)
        INSERT INTO permissions (code, description, created_at, updated_at)
        VALUES 
            ('order.void', 'Ability to void orders', NOW(), NOW()),
            ('order.refund', 'Ability to process refunds', NOW(), NOW()),
            ('order.create', 'Ability to create orders', NOW(), NOW()),
            ('order.view', 'Ability to view orders', NOW(), NOW()),
            ('order.update', 'Ability to update orders', NOW(), NOW()),
            ('order.delete', 'Ability to delete orders', NOW(), NOW()),
            ('order.return', 'Ability to process order returns', NOW(), NOW()),
            ('product.edit_price', 'Ability to edit product prices', NOW(), NOW()),
            ('product.create', 'Ability to create new products', NOW(), NOW()),
            ('product.view', 'Ability to view products', NOW(), NOW()),
            ('product.update', 'Ability to update products', NOW(), NOW()),
            ('product.delete', 'Ability to delete products', NOW(), NOW()),
            ('stock.adjust', 'Ability to adjust stock levels', NOW(), NOW()),
            ('stock.transfer', 'Ability to transfer stock between branches', NOW(), NOW()),
            ('purchase.create', 'Ability to create purchase orders', NOW(), NOW()),
            ('purchase.approve', 'Ability to approve purchase orders', NOW(), NOW()),
            ('purchase.view', 'Ability to view purchase orders', NOW(), NOW()),
            ('purchase.update', 'Ability to update purchase orders', NOW(), NOW()),
            ('purchase.submit', 'Ability to submit purchase orders', NOW(), NOW()),
            ('purchase.cancel', 'Ability to cancel purchase orders', NOW(), NOW()),
            ('purchase.delete', 'Ability to delete purchase orders', NOW(), NOW()),
            ('purchase.receive', 'Ability to receive goods', NOW(), NOW()),
            ('payment.create', 'Ability to create payments', NOW(), NOW()),
            ('payment.view', 'Ability to view payments', NOW(), NOW()),
            ('payment.update', 'Ability to update payments', NOW(), NOW()),
            ('payment.delete', 'Ability to delete payments', NOW(), NOW()),
            ('shift.open', 'Ability to open shifts', NOW(), NOW()),
            ('shift.close', 'Ability to close shifts', NOW(), NOW()),
            ('shift.reconcile', 'Ability to reconcile shifts', NOW(), NOW()),
            ('shift.view', 'Ability to view shifts', NOW(), NOW()),
            ('shift.delete', 'Ability to delete shifts', NOW(), NOW()),
            ('user.view', 'Ability to view users', NOW(), NOW()),
            ('user.create', 'Ability to create users', NOW(), NOW()),
            ('user.update', 'Ability to update users', NOW(), NOW()),
            ('user.delete', 'Ability to delete users', NOW(), NOW()),
            ('user.manage', 'Ability to manage users', NOW(), NOW()),
            ('role.view', 'Ability to view roles', NOW(), NOW()),
            ('role.create', 'Ability to create roles', NOW(), NOW()),
            ('role.update', 'Ability to update roles', NOW(), NOW()),
            ('role.delete', 'Ability to delete roles', NOW(), NOW()),
            ('role.manage', 'Ability to manage roles', NOW(), NOW()),
            ('branch.manage', 'Ability to manage branches', NOW(), NOW()),
            ('branch.settings.view', 'Ability to view branch settings', NOW(), NOW()),
            ('branch.settings.update', 'Ability to update branch settings', NOW(), NOW()),
            ('settings.manage', 'Ability to manage settings', NOW(), NOW()),
            ('report.view', 'Ability to view reports', NOW(), NOW()),
            ('audit.view', 'Ability to view audit logs', NOW(), NOW()),
            ('customer.view', 'Ability to view customers', NOW(), NOW()),
            ('customer.create', 'Ability to create customers', NOW(), NOW()),
            ('customer.update', 'Ability to update customers', NOW(), NOW()),
            ('customer.delete', 'Ability to delete customers', NOW(), NOW()),
            ('customer.manage', 'Ability to manage customers', NOW(), NOW()),
            ('doctor.view', 'Ability to view doctors', NOW(), NOW()),
            ('doctor.create', 'Ability to create doctors', NOW(), NOW()),
            ('doctor.update', 'Ability to update doctors', NOW(), NOW()),
            ('doctor.delete', 'Ability to delete doctors', NOW(), NOW()),
            ('doctor.manage', 'Ability to manage doctors', NOW(), NOW()),
            ('prescription.view', 'Ability to view prescriptions', NOW(), NOW()),
            ('prescription.create', 'Ability to create prescriptions', NOW(), NOW()),
            ('prescription.update', 'Ability to update prescriptions', NOW(), NOW()),
            ('prescription.delete', 'Ability to delete prescriptions', NOW(), NOW()),
            ('prescription.manage', 'Ability to manage prescriptions', NOW(), NOW()),
            ('device.view', 'Ability to view devices', NOW(), NOW()),
            ('device.create', 'Ability to create devices', NOW(), NOW()),
            ('device.update', 'Ability to update devices', NOW(), NOW()),
            ('device.delete', 'Ability to delete devices', NOW(), NOW()),
            ('goods_receipt.view', 'Ability to view goods receipts', NOW(), NOW()),
            ('goods_receipt.create', 'Ability to create goods receipts', NOW(), NOW()),
            ('goods_receipt.delete', 'Ability to delete goods receipts', NOW(), NOW())
        ON CONFLICT (code) DO NOTHING;
        
        -- 6. Role Permissions
        
        -- Owner: ALL permissions
        INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
        SELECT owner_role_id, id, NOW(), NOW() FROM permissions
        ON CONFLICT (role_id, permission_id) DO NOTHING;
        
        -- Manager: everything except user.manage, role.manage, branch.manage, settings.manage
        INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
        SELECT manager_role_id, id, NOW(), NOW() FROM permissions
        WHERE code NOT IN ('user.manage', 'role.manage', 'branch.manage', 'settings.manage')
        ON CONFLICT (role_id, permission_id) DO NOTHING;
        
        -- Pharmacist: order.void, stock.adjust, shift.open, shift.close, report.view, customer.view, doctor.view, prescription.view
        INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
        SELECT pharmacist_role_id, id, NOW(), NOW() FROM permissions
        WHERE code IN ('order.void', 'stock.adjust', 'shift.open', 'shift.close', 'report.view', 'customer.view', 'doctor.view', 'prescription.view')
        ON CONFLICT (role_id, permission_id) DO NOTHING;
        
        -- Cashier: shift.open, shift.close
        INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
        SELECT cashier_role_id, id, NOW(), NOW() FROM permissions
        WHERE code IN ('shift.open', 'shift.close')
        ON CONFLICT (role_id, permission_id) DO NOTHING;
        
        RAISE NOTICE 'Seed data inserted successfully';
    ELSE
        RAISE NOTICE 'Organizations already exist, skipping seed data insertion';
    END IF;
END $$;