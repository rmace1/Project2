INSERT INTO users VALUES(DEFAULT, 'john.doe@gmail.com', 'John', 'Doe', 'ialDnjRQtMdHjTTwkAmTnZyuoqX5MPPxh0FJ5iDIjBk7BB7XDICH+OMeWqUH8EdD', 's3://jwa-p2/SocialNetwork/john.doe/Screenshot 2020-10-25 at 8.35.20 PM.jpeg', 'jdoe1');
INSERT INTO users VALUES(DEFAULT, 'jane.doe@gmail.com', 'Jane', 'Doe', 'ialDnjRQtMdHjTTwkAmTnZyuoqX5MPPxh0FJ5iDIjBk7BB7XDICH+OMeWqUH8EdD', 's3://jwa-p2/SocialNetwork/jane.doe/Screenshot 2020-10-22 at 7.58.53 PM.jpeg', 'jdoe2');
/* the password = pass123 */

INSERT INTO posts VALUES(DEFAULT, DEFAULT, 'This is my first post with an image!', 's3://jwa-p2/SocialNetwork/john.doe/49290380-29b5-4e4d-967d-f99b68050358.png', DEFAULT, 1, DEFAULT);
INSERT INTO posts VALUES(DEFAULT, DEFAULT, 'I love how this picture looks!', 's3://jwa-p2/SocialNetwork/john.doe/CIT240RichardMaceFinalSkills.PNG', DEFAULT, 2, DEFAULT);
INSERT INTO posts VALUES(DEFAULT, DEFAULT, 'I agree, it is lovely!', DEFAULT, DEFAULT, 1, 2);
INSERT INTO posts VALUES(DEFAULT, DEFAULT, 'Thanks!  I really appreciate it.', DEFAULT, DEFAULT, 2, 2);


INSERT INTO user_post values(1,2);
UPDATE posts SET likes = likes + 1 WHERE id = 2;

INSERT INTO user_post values(1,3);
UPDATE posts SET likes = likes + 1 WHERE id = 3;