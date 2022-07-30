CREATE TABLE access_control_users(
  username varchar(40),
  password varchar(40),
  admin boolean
);

INSERT INTO access_control_users VALUES ('Tom', 'qwertyqwerty1234', false);
INSERT INTO access_control_users VALUES ('Jerry', 'doesnotreallymatter', true);
INSERT INTO access_control_users VALUES ('Sylvester', 'testtesttest', false);
