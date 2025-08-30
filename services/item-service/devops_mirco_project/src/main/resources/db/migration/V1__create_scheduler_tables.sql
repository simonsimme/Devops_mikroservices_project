CREATE TABLE roles(
    name TEXT PRIMARY KEY
);

CREATE TABLE worker(
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    role TEXT NOT NULL REFERENCES roles(name)
        , user_id UUID UNIQUE -- references user-service user id
);

CREATE TABLE shift(
    id UUID PRIMARY KEY,
    date DATE NOT NULL,
    worker_id UUID REFERENCES worker(id),
    required_role TEXT NOT NULL REFERENCES roles(name),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);

CREATE TABLE shiftassignment(
    id UUID PRIMARY KEY,
    shift_id UUID NOT NULL REFERENCES shift(id),
    worker_id UUID NOT NULL REFERENCES worker(id),
    assigned_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO roles (name) VALUES 
('floor'),
('floor-manager'), 
('administration'),
('manager');