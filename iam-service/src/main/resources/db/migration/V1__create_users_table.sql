
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Personal information
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    avatar_url TEXT,
    phone_number VARCHAR(30),
    date_of_birth DATE,
    gender VARCHAR(20),

    -- Account status
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_VERIFICATION',

    -- Verification and authentication
    email_verified_at TIMESTAMPTZ,
    password_changed_at TIMESTAMPTZ,
    last_login_at TIMESTAMPTZ,

    -- Login security
    failed_attempts INTEGER NOT NULL DEFAULT 0,
    failed_sign_in_at TIMESTAMPTZ,
    locked_at TIMESTAMPTZ,

    -- Audit timestamps
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,

    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_failed_attempts
       CHECK (failed_attempts >= 0)
);

-- Indexes
CREATE INDEX idx_users_status
    ON users(status);

CREATE INDEX idx_users_last_name
    ON users(last_name);
