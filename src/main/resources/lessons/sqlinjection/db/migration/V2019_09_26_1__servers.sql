CREATE TABLE SERVERS(
  id varchar(10),
  hostname varchar(20),
  ip varchar(20),
  mac varchar(20),
  status varchar(20),
  description varchar(40)
);
INSERT INTO SERVERS VALUES ('1', 'webgoat-dev', '192.168.4.0', 'AA:BB:11:22:CC:DD', 'online', 'Development server');
INSERT INTO SERVERS VALUES ('2', 'webgoat-tst', '192.168.2.1', 'EE:FF:33:44:AB:CD', 'online', 'Test server');
INSERT INTO SERVERS VALUES ('3', 'webgoat-acc', '192.168.3.3', 'EF:12:FE:34:AA:CC', 'offline', 'Acceptance server');
INSERT INTO SERVERS VALUES ('4', 'webgoat-pre-prod', '192.168.6.4', 'EF:12:FE:34:AA:CC', 'offline', 'Pre-production server');
INSERT INTO SERVERS VALUES ('4', 'webgoat-prd', '104.130.219.202', 'FA:91:EB:82:DC:73', 'out of order', 'Production server');
