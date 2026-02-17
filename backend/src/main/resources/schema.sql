CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY,
    username TEXT UNIQUE,
    password_hash TEXT,
    guest BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    vin TEXT,
    make TEXT,
    model TEXT,
    year INTEGER,
    license_plate TEXT,
    mileage INTEGER
);

CREATE TABLE IF NOT EXISTS maintenance_records (
    vehicle_id UUID NOT NULL,
    service_date DATE,
    service_type_name TEXT,
    service_type_default_expiry_miles INTEGER,
    service_type_default_expiry_time TEXT,
    mileage INTEGER,
    expiry_mileage INTEGER,
    expiry_date DATE,
    notes TEXT
);
