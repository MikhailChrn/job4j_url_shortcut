CREATE TABLE users (
    id               SERIAL PRIMARY KEY,
    email            TEXT   NOT NULL    UNIQUE,
    password         TEXT
);

CREATE TABLE roles (
    id              SERIAL PRIMARY KEY,
    title           TEXT    UNIQUE
);

CREATE TABLE users_roles (
    id              SERIAL PRIMARY KEY,
    user_id         INT    NOT NULL REFERENCES  users(id),
    role_id         INT    NOT NULL REFERENCES  roles(id)
);