CREATE TABLE user_system_data(
  userid int not null primary key,
  user_name varchar(12),
  password varchar(10),
  cookie varchar(30)
);

INSERT INTO user_system_data VALUES (101,'jsnow','passwd1', '');
INSERT INTO user_system_data VALUES (102,'jdoe','passwd2', '');
INSERT INTO user_system_data VALUES (103,'jplane','passwd3', '');
INSERT INTO user_system_data VALUES (104,'jeff','jeff', '');
INSERT INTO user_system_data VALUES (105,'dave','passW0rD', '');