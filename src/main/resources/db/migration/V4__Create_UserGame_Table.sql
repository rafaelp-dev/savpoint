CREATE TABLE user_game (
    user_game_id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    status VARCHAR(50),
    favorite BOOLEAN DEFAULT false,
    rating INTEGER,
    review TEXT,
    added_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_usergame_profile FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id) ON DELETE CASCADE,
    CONSTRAINT fk_usergame_game FOREIGN KEY (game_id) REFERENCES games(game_id) ON DELETE CASCADE,
    UNIQUE(user_profile_id, game_id)
);

CREATE INDEX idx_usergame_profile_id ON user_game(user_profile_id);
CREATE INDEX idx_usergame_game_id ON user_game(game_id);
CREATE INDEX idx_usergame_status ON user_game(status);