CREATE TABLE user_data_tan (
 userid int not null,
 first_name varchar(20),
 last_name varchar(20),
 cc_number varchar(30),
 cc_type varchar(10),
 cookie varchar(20),
 login_count int,
 password varchar(20)
);

INSERT INTO user_data_tan VALUES (101,'Joe','Snow','987654321','VISA',' ',0, 'banana');
INSERT INTO user_data_tan VALUES (102,'Jane','Plane','74589864','MC',' ',0, 'tarzan');
INSERT INTO user_data_tan VALUES (103,'Jack','Sparrow','68659365','MC',' ',0, 'sniffy');