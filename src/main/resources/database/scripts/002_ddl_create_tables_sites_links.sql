CREATE TABLE sites (
    id               SERIAL PRIMARY KEY,
    domain_name      TEXT   NOT NULL    UNIQUE,
    total            INT,
    person_id        INT    REFERENCES  persons(id)
);

CREATE TABLE links (
    id               SERIAL PRIMARY KEY,
    code             TEXT   UNIQUE,
    orig_url         TEXT   NOT NULL    UNIQUE,
    total            INT,
    site_id          INT    REFERENCES  sites(id)
);