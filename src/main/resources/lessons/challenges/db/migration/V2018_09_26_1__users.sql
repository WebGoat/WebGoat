--Challenge 5 - Creating tables for users
CREATE TABLE challenge_users(
  userid varchar(250),
  email varchar(30),
  password varchar(30)
);

INSERT INTO challenge_users VALUES ('larry', 'larry@webgoat.org', 'larryknows');
INSERT INTO challenge_users VALUES ('tom', 'tom@webgoat.org', 'thisisasecretfortomonly');
INSERT INTO challenge_users VALUES ('alice', 'alice@webgoat.org', 'rt*(KJ()LP())$#**');
INSERT INTO challenge_users VALUES ('eve', 'eve@webgoat.org', '**********');
