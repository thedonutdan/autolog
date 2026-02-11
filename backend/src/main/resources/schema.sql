CREATE TABLE IF NOT EXISTS users (
    user_id TEXT PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    guest BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    vin TEXT,
    make TEXT,
    model TEXT,
    year INTEGER,
    license_plate TEXT,
    mileage INTEGER
);

CREATE TABLE IF NOT EXISTS maintenance_records (
    vehicle_id TEXT NOT NULL,
    date TEXT,
    service_type_name TEXT,
    service_type_default_expiry_miles INTEGER,
    service_type_default_expiry_time TEXT,
    mileage INTEGER,
    expiry_mileage INTEGER,
    expiry_date TEXT,
    notes TEXT
);
