INSERT INTO users values(DEFAULT, 'rmace1@', 'richard', 'mace', 'ialDnjRQtMdHjTTwkAmTnZyuoqX5MPPxh0FJ5iDIjBk7BB7XDICH+OMeWqUH8EdD', NULL, 'rmace1');
INSERT INTO users values(DEFAULT, 'rmace2@', 'richard', 'mace', 'ialDnjRQtMdHjTTwkAmTnZyuoqX5MPPxh0FJ5iDIjBk7BB7XDICH+OMeWqUH8EdD', NULL, 'rmace2');
INSERT INTO users values(DEFAULT, 'rmace3@', 'richard', 'mace', 'ialDnjRQtMdHjTTwkAmTnZyuoqX5MPPxh0FJ5iDIjBk7BB7XDICH+OMeWqUH8EdD', NULL, 'rmace3');
INSERT INTO users values(DEFAULT, 'rmace4@', 'richard', 'mace', 'ialDnjRQtMdHjTTwkAmTnZyuoqX5MPPxh0FJ5iDIjBk7BB7XDICH+OMeWqUH8EdD', NULL, 'rmace4');
/*password= pass123 */
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my first post!', NULL, DEFAULT, 1, null);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my wonderful first post!', NULL, DEFAULT, 2, null);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my successful first post!', NULL, DEFAULT, 3, null);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my guaranteed first post!', NULL, DEFAULT, 4, null);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my lovely second post!', NULL, DEFAULT, 1, null);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my first about my own comment!', NULL, DEFAULT, 1, 1);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my first lovely comment!', NULL, DEFAULT, 3, 1);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my first gorgeous comment!', NULL, DEFAULT, 2, 1);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my first well thought out comment!', NULL, DEFAULT, 4, 1);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my first comment', NULL, now(), 1, 4);
INSERT INTO posts values(DEFAULT, DEFAULT, 'this is my first well thought out comment on a comment!', NULL, DEFAULT, 4, 9);

INSERT INTO user_post values(1,4);
UPDATE posts SET likes = likes + 1 WHERE id = 4 ;

INSERT INTO user_post values(1,3);
INSERT INTO user_post values(1,6);

INSERT INTO user_post values(1,2);
UPDATE posts SET likes = likes + 1 WHERE id = 4 ;