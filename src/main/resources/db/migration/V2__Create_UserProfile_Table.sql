CREATE TABLE user_profile (
    user_profile_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    credits DECIMAL(19,2) NOT NULL DEFAULT 0,
    UNIQUE(user_id),
    CONSTRAINT fk_userprofile_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_userprofile_user_id ON user_profile(user_id);
