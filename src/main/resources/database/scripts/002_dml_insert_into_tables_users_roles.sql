INSERT INTO roles (id, title) VALUES (1, 'ROLE_ADMIN'),
                                    (2, 'ROLE_USER');

INSERT INTO users (id, email, password) VALUES (1, 'admin', 'admin'),
                                                (2, 'user', 'user');

INSERT INTO users_roles (id, user_id, role_id) VALUES (1, 1, 1),
                                                        (2, 2, 2);