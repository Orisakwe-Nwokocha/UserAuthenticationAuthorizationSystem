TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE blacklisted_tokens CASCADE;

INSERT INTO users(id, username, password, date_registered) VALUES
(100, 'user', '$2a$10$6rIpDTj3/hiYiHdnzooaWuSjGTZT8C88aIuRlo9Lph./ZY71fsl5S', '2024-07-02 23:41:10.614686'),
(101, 'admin', '$2a$10$6rIpDTj3/hiYiHdnzooaWuSjGTZT8C88aIuRlo9Lph./ZY71fsl5S', '2024-07-02 23:41:10.614686');

INSERT INTO user_roles(user_id, roles)VALUES
(100, 'USER'),
(101, 'ADMIN');

INSERT INTO blacklisted_tokens (id, token, expires_at, blacklisted_at) values
(200, 'eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJqd3QtcHJvamVjdCIsImlhdCI6MTcyMDg3NTc0NywiZXhwIjoxNzIwODc5MzQ3LCJzdWIiOiJhZG1pbiIsInByaW5jaXBhbCI6ImFkbWluIiwiY3JlZGVudGlhbHMiOiJbUFJPVEVDVEVEXSIsInJvbGVzIjpbIkFETUlOIl19.QGMnCxOGMbG8-BoPz2Zf2OGiTAy4iVC0mUKhzKS6vj007zNSMMNTrQBVnFQrzQPJg6mMw2xu6ZxVNS9EmLo2s19fFGMPKXGaEx4UWq8W-w_XyQG-oS6916pz7dNb0twKt8T9VvCe-uicllXjx3-ok5M-L-cpyE3_3Bc-aTMWfzGI0FU6Vi1zTykE_NvvKvJUKVyMIWlV7JV3pbPZTgzjdRCkkU-pw7WOajTJU54ngNI89-wq51oH9yyBlQukiURGLugpXpv6y8EXjZA-s21USLOWPyaGx5_ae1Ac8qft4-a9yLH5YJh-hw39kHG7PG-PNd7d5NLhf3kaahgdAB_99g',
 '2024-07-13T14:02:27.425305100Z', '2024-07-13T14:02:27.434315200');