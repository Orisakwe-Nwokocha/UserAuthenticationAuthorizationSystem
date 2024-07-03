TRUNCATE TABLE users CASCADE;

INSERT INTO users(id, username, password, date_registered) VALUES
(100, 'user', '$2a$10$6rIpDTj3/hiYiHdnzooaWuSjGTZT8C88aIuRlo9Lph./ZY71fsl5S', '2024-07-02 23:41:10.614686'),
(101, 'admin', '$2a$10$6rIpDTj3/hiYiHdnzooaWuSjGTZT8C88aIuRlo9Lph./ZY71fsl5S', '2024-07-02 23:41:10.614686');

INSERT INTO user_roles(user_id, roles)VALUES
(100, 'USER'),
(101, 'ADMIN');