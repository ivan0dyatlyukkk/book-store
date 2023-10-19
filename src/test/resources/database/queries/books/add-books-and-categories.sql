INSERT INTO books
    (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (1, 'Test book #1', 'Bob Bobenko', '978-0-16-251140-9', 200, 'This is a description', 'link.ua//images/1', false),
    (2, 'Test book #2', 'Ivan Ivanenko', '978-0-16-251141-9', 100, 'This is a description', 'link.ua//images/2', false),
    (3, 'Test book #3', 'Bob Bobenko', '978-0-16-261150-9', 150, 'This is a description', 'link.ua//images/3', false);

INSERT INTO categories
    (id, name, description, is_deleted)
VALUES
    (1, 'Fiction', 'Fiction books', false),
    (2, 'Classic Literature', 'Timeless literary masterpieces', false);

INSERT INTO books_categories
    (book_id, category_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 2);
