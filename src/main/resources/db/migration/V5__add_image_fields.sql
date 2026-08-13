-- ============================================================================
-- PHARMACY POS - ADD IMAGE FIELDS MIGRATION
-- This migration adds image_url columns to users, products, customers, and doctors tables
-- ============================================================================

-- Add image_url column to users table
ALTER TABLE users ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

-- Add image_url column to products table
ALTER TABLE products ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

-- Add image_url column to customers table
ALTER TABLE customers ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

-- Add image_url column to doctors table
ALTER TABLE doctors ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

-- Add comments for documentation
COMMENT ON COLUMN users.image_url IS 'URL to user profile image stored in Cloudinary';
COMMENT ON COLUMN products.image_url IS 'URL to product image stored in Cloudinary';
COMMENT ON COLUMN customers.image_url IS 'URL to customer photo stored in Cloudinary';
COMMENT ON COLUMN doctors.image_url IS 'URL to doctor photo stored in Cloudinary';
