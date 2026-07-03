UPDATE games SET release_date = '1970-01-01' WHERE release_date IS NULL;
UPDATE games SET cover_url = 'sem url' WHERE cover_url IS NULL;

ALTER TABLE games ALTER COLUMN release_date SET NOT NULL;
ALTER TABLE games ALTER COLUMN cover_url SET NOT NULL;
ALTER TABLE games ALTER COLUMN playtime DROP NOT NULL;