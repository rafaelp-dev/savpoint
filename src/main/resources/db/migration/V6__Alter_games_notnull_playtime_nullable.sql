ALTER TABLE games ALTER COLUMN playtime TYPE integer USING (CASE WHEN playtime ~ '^[0-9]+$' THEN playtime::integer ELSE NULL END);

ALTER TABLE games ALTER COLUMN playtime DROP NOT NULL;

ALTER TABLE games ADD COLUMN IF NOT EXISTS completed_info boolean NOT NULL DEFAULT false;