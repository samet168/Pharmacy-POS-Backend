-- Add organizationId and nameKh to active_ingredients table
ALTER TABLE active_ingredients ADD COLUMN IF NOT EXISTS organization_id BIGINT NOT NULL REFERENCES organizations(id);
ALTER TABLE active_ingredients ADD COLUMN IF NOT EXISTS name_kh VARCHAR;