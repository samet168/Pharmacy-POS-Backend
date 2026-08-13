-- ============================================================================
-- PHARMACY POS - ADD MISSING PERMISSIONS MIGRATION
-- This migration adds the 49 missing permissions that were added to V2
-- but weren't inserted due to ON CONFLICT DO NOTGHING
-- ============================================================================

-- Only run if the permissions count is less than expected
DO $$
BEGIN
    -- Check if we need to add missing permissions (count should be 72, but might be 23)
    DECLARE current_perm_count INT;
    SELECT COUNT(*) INTO current_perm_count FROM permissions;
    
    IF current_perm_count < 72 THEN
        RAISE NOTICE 'Adding missing permissions. Current count: %', current_perm_count;
        
        -- Add missing permissions
        INSERT INTO permissions (code, description, created_at, updated_at)
        VALUES 
            ('order.create', 'Ability to create orders', NOW(), NOW()),
            ('order.view', 'Ability to view orders', NOW(), NOW()),
            ('order.update', 'Ability to update orders', NOW(), NOW()),
            ('order.delete', 'Ability to delete orders', NOW(), NOW()),
            ('order.return', 'Ability to process order returns', NOW(), NOW()),
            ('product.view', 'Ability to view products', NOW(), NOW()),
            ('product.update', 'Ability to update products', NOW(), NOW()),
            ('product.delete', 'Ability to delete products', NOW(), NOW()),
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
            ('shift.view', 'Ability to view shifts', NOW(), NOW()),
            ('shift.delete', 'Ability to delete shifts', NOW(), NOW()),
            ('user.manage', 'Ability to manage users', NOW(), NOW()),
            ('role.manage', 'Ability to manage roles', NOW(), NOW()),
            ('branch.settings.view', 'Ability to view branch settings', NOW(), NOW()),
            ('branch.settings.update', 'Ability to update branch settings', NOW(), NOW()),
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
        
        -- Get role IDs
        DECLARE owner_role_id BIGINT;
        DECLARE manager_role_id BIGINT;
        DECLARE pharmacist_role_id BIGINT;
        DECLARE cashier_role_id BIGINT;
        
        SELECT id INTO owner_role_id FROM roles WHERE name = 'Owner' AND is_system_role = true;
        SELECT id INTO manager_role_id FROM roles WHERE name = 'Manager' AND is_system_role = true;
        SELECT id INTO pharmacist_role_id FROM roles WHERE name = 'Pharmacist' AND is_system_role = true;
        SELECT id INTO cashier_role_id FROM roles WHERE name = 'Cashier' AND is_system_role = true;
        
        -- Assign new permissions to Owner role (ALL permissions)
        INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
        SELECT owner_role_id, id, NOW(), NOW() FROM permissions
        WHERE code IN (
            'order.create', 'order.view', 'order.update', 'order.delete', 'order.return',
            'product.view', 'product.update', 'product.delete',
            'purchase.view', 'purchase.update', 'purchase.submit', 'purchase.cancel', 'purchase.delete', 'purchase.receive',
            'payment.create', 'payment.view', 'payment.update', 'payment.delete',
            'shift.view', 'shift.delete',
            'user.manage', 'role.manage',
            'branch.settings.view', 'branch.settings.update',
            'customer.view', 'customer.create', 'customer.update', 'customer.delete', 'customer.manage',
            'doctor.view', 'doctor.create', 'doctor.update', 'doctor.delete', 'doctor.manage',
            'prescription.view', 'prescription.create', 'prescription.update', 'prescription.delete', 'prescription.manage',
            'device.view', 'device.create', 'device.update', 'device.delete',
            'goods_receipt.view', 'goods_receipt.create', 'goods_receipt.delete'
        )
        ON CONFLICT (role_id, permission_id) DO NOTHING;
        
        -- Assign new permissions to Manager role (all except user.manage, role.manage, branch.manage, settings.manage)
        INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
        SELECT manager_role_id, id, NOW(), NOW() FROM permissions
        WHERE code IN (
            'order.create', 'order.view', 'order.update', 'order.delete', 'order.return',
            'product.view', 'product.update', 'product.delete',
            'purchase.view', 'purchase.update', 'purchase.submit', 'purchase.cancel', 'purchase.delete', 'purchase.receive',
            'payment.create', 'payment.view', 'payment.update', 'payment.delete',
            'shift.view', 'shift.delete',
            'branch.settings.view', 'branch.settings.update',
            'customer.view', 'customer.create', 'customer.update', 'customer.delete', 'customer.manage',
            'doctor.view', 'doctor.create', 'doctor.update', 'doctor.delete', 'doctor.manage',
            'prescription.view', 'prescription.create', 'prescription.update', 'prescription.delete', 'prescription.manage',
            'device.view', 'device.create', 'device.update', 'device.delete',
            'goods_receipt.view', 'goods_receipt.create', 'goods_receipt.delete'
        )
        ON CONFLICT (role_id, permission_id) DO NOTHING;
        
        -- Assign new permissions to Pharmacist role (order.void, stock.adjust, shift.open, shift.close, report.view, customer.view, doctor.view, prescription.view)
        INSERT INTO role_permissions (role_id, permission_id, created_at, updated_at)
        SELECT pharmacist_role_id, id, NOW(), NOW() FROM permissions
        WHERE code IN (
            'customer.view', 'doctor.view', 'prescription.view'
        )
        ON CONFLICT (role_id, permission_id) DO NOTHING;
        
        -- Cashier: no new permissions needed (only shift.open, shift.close which they already have)
        
        RAISE NOTICE 'Missing permissions added successfully. New count: %', (SELECT COUNT(*) FROM permissions);
    ELSE
        RAISE NOTICE 'All permissions already exist. Current count: %', current_perm_count;
    END IF;
END $$;
