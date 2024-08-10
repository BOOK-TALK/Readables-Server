INSERT IGNORE INTO opentalk (opentalk_id, book_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

INSERT IGNORE INTO book (book_id, isbn, genre_id)
VALUES (1, '9788960880041', 1),
       (2, '9788960880058', 2),
       (3, '9788960880065', 3),
       (4, '9788960880072', 4),
       (5, '9788960880089', 5);

INSERT IGNORE INTO user (user_id, login_id, password, gender, birth_date, email, phone, reg_date)
values (1, 'user1', 'useruser1!', '1', '1990-01-01', 'user1@gmail.com', '010-1111-1111', '2024-06-10'),
       (2, 'user2', 'useruser2!', '2', '2000-03-16', 'user2@gmail.com', '010-2222-2222', '2024-07-22');

INSERT IGNORE INTO user_opentalk(user_opentalk_id, user_id, opentalk_id)
values (1, 1, 1),
       (2, 1, 2),
       (3, 2, 2),
       (4, 2, 4),
       (5, 2, 5);

