CREATE TABLE employees(
  userid varchar(6) not null primary key,
  first_name varchar(20),
  last_name varchar(20),
  department varchar(20),
  salary int,
  auth_tan varchar(6)
);

INSERT INTO employees VALUES ('32147','Paulina',  'Travers', 'Accounting',  46000, 'P45JSI');
INSERT INTO employees VALUES ('89762','Tobi',     'Barnett', 'Development', 77000, 'TA9LL1');
INSERT INTO employees VALUES ('96134','Bob',      'Franco',  'Marketing',   83700, 'LO9S2V');
INSERT INTO employees VALUES ('34477','Abraham ', 'Holman',  'Development', 50000, 'UU2ALK');
INSERT INTO employees VALUES ('37648','John',     'Smith',   'Marketing',   64350, '3SL99A');

CREATE TABLE access_log (
  id int generated always as identity not null primary key,
  time varchar(50),
  action varchar(200)
);
