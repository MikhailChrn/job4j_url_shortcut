CREATE TABLE sites (
    id               SERIAL PRIMARY KEY,
    domain_name      TEXT   NOT NULL    UNIQUE,
    total            INT,
    user_id          INT    REFERENCES  users(id)
);

CREATE TABLE links (
    id               SERIAL PRIMARY KEY,
    orig_url         TEXT,
    resp_key         TEXT,
    total            INT,
    site_id          INT    REFERENCES  sites(id)
);