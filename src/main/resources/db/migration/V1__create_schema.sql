CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    iban VARCHAR(34) NOT NULL UNIQUE,
    owner_first_name VARCHAR(100) NOT NULL,
    owner_last_name VARCHAR(100) NOT NULL,
    balance_cents BIGINT NOT NULL,
    overdraft_limit_cents BIGINT NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    card_number VARCHAR(32) NOT NULL UNIQUE,
    card_type VARCHAR(32) NOT NULL,
    pin_hash VARCHAR(255) NOT NULL,
    expires_on DATE NOT NULL,
    status VARCHAR(32) NOT NULL,
    failed_pin_attempts INTEGER NOT NULL,
    account_id BIGINT REFERENCES accounts(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE cash_cassette_slots (
    id BIGSERIAL PRIMARY KEY,
    denomination_cents INTEGER NOT NULL UNIQUE,
    note_count INTEGER NOT NULL
);

CREATE TABLE atm_sessions (
    id UUID PRIMARY KEY,
    card_id BIGINT NOT NULL REFERENCES cards(id),
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    language VARCHAR(8) NOT NULL,
    authenticated BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    finished_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE withdrawals (
    id BIGSERIAL PRIMARY KEY,
    card_id BIGINT NOT NULL REFERENCES cards(id),
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    amount_cents BIGINT NOT NULL,
    fee_cents BIGINT NOT NULL,
    total_debit_cents BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    business_date DATE NOT NULL,
    status VARCHAR(32) NOT NULL,
    decline_reason VARCHAR(255)
);

