INSERT INTO users
    (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES
    (4, 'test@gmail.com', 'test0test', 'test', 'tester', 'shippingAddress', false);

INSERT INTO users_roles
    (user_id, role_id)
VALUES
    (4, 2);

INSERT INTO shopping_carts
    (id, user_id)
VALUES
    (1, 4);

INSERT INTO books
(id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (4, 'Test', 'ADMIN ADMIN', '978-161-729-045-9', 200, 'This is a description', 'link.ua//images/1', false),
    (5, 'The book 5', 'ADMIN ADMIN', '978-161-729-045-7', 200, 'This is a description', 'link.ua//images/1', false);

INSERT INTO cart_items
    (id, shopping_cart_id, book_id, quantity, is_deleted)
VALUES
    (1, 1, 4, 1, false);

