CREATE TABLE games (
    game_id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE,
    cover_url TEXT,
    playtime VARCHAR(255) NOT NULL,
    plataforms VARCHAR(255) NOT NULL,
    genres VARCHAR(255) NOT NULL,
    developers VARCHAR(255),
    publisher VARCHAR(255),
    added_at TIMESTAMP NOT NULL,
    UNIQUE(external_id),
    UNIQUE(slug)
);

CREATE INDEX idx_game_external_id ON games(external_id);
CREATE INDEX idx_game_slug ON games(slug);
CREATE INDEX idx_game_added_at ON games(added_at);
