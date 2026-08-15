-- Add nameKh and isActive to categories table
ALTER TABLE categories ADD COLUMN IF NOT EXISTS name_kh VARCHAR;
ALTER TABLE categories ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT true;

-- Add taxId to suppliers table
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS tax_id VARCHAR;