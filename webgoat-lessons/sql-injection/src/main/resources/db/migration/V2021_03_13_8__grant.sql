CREATE TABLE grant_rights(
  userid varchar(6) not null primary key,
  first_name varchar(20),
  last_name varchar(20),
  department varchar(20),
  salary int
);

INSERT INTO grant_rights VALUES ('32147','Paulina',  'Travers', 'Accounting',  46000);
INSERT INTO grant_rights VALUES ('89762','Tobi',     'Barnett', 'Development', 77000);
INSERT INTO grant_rights VALUES ('96134','Bob',      'Franco',  'Marketing',   83700);
INSERT INTO grant_rights VALUES ('34477','Abraham ', 'Holman',  'Development', 50000);
INSERT INTO grant_rights VALUES ('37648','John',     'Smith',   'Marketing',   64350);

