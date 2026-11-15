
CREATE TABLE verification_tokens (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     user_id UUID NOT NULL,
     token_hash VARCHAR(255) NOT NULL,
     type VARCHAR(50) NOT NULL,
     expires_at TIMESTAMPTZ NOT NULL,
     verified_at TIMESTAMPTZ,
     revoked_at TIMESTAMPTZ,

     -- Audit timestamps
     created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
     deleted_at TIMESTAMPTZ,

     CONSTRAINT fk_verification_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
     CONSTRAINT uq_verification_tokens_token_hash UNIQUE (token_hash)
);

-- Index for looking up a token
CREATE INDEX idx_verification_tokens_token_hash ON verification_tokens (token_hash);
-- Index for retrieving a user's verification tokens
CREATE INDEX idx_verification_tokens_user_id ON verification_tokens (user_id);
-- Index for retrieving tokens by type
CREATE INDEX idx_verification_tokens_user_type ON verification_tokens (user_id, type);
-- Index for expiration cleanup
CREATE INDEX idx_verification_tokens_expires_at ON verification_tokens (expires_at);
