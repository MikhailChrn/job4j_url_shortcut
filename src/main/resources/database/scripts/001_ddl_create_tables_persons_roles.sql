CREATE TABLE persons (
    id               SERIAL PRIMARY KEY,
    username         TEXT   NOT NULL    UNIQUE,
    password         TEXT
);

CREATE TABLE roles (
    id              SERIAL PRIMARY KEY,
    title           TEXT   UNIQUE
);

CREATE TABLE persons_roles (
    id              SERIAL PRIMARY KEY,
    person_id       INT    NOT NULL REFERENCES  persons(id),
    role_id         INT    NOT NULL REFERENCES  roles(id)
);