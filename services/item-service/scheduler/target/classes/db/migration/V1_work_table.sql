CREATE TABLE Roles(
    name TEXT PRIMARY KEY CHECK (name IN ('floor', 'floor-manager', 'administration', 'manager'))
);

CREATE TABLE IF NOT EXISTS Worker(
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    role TEXT NOT NULL REFERENCES Roles(name)
);


CREATE TABLE IF NOT EXISTS Shift(
    id UUID PRIMARY KEY,
    date DATE NOT NULL,
    worker_id UUID REFERENCES Worker(id), --null means free
    required_role TEXT NOT NULL REFERENCES Roles(name),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ShiftAssignment(
    id UUID PRIMARY KEY,
    shift_id UUID NOT NULL REFERENCES Shift(id),
    worker_id UUID NOT NULL REFERENCES Worker(id),
    assigned_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_shift_worker_id ON Shift(worker_id);
CREATE INDEX IF NOT EXISTS idx_shift_date ON Shift(date);