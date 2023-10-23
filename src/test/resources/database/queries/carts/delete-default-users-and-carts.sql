DELETE FROM cart_items WHERE cart_items.shopping_cart_id = 1;

DELETE FROM books;

DELETE FROM shopping_carts WHERE shopping_carts.id = 1;

DELETE FROM users_roles;

DELETE FROM users WHERE users.email = 'test@gmail.com';
