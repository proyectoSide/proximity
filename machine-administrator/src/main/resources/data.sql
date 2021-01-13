DROP TABLE IF EXISTS machine;
CREATE TABLE machine (
 id INT AUTO_INCREMENT  PRIMARY KEY,
 host VARCHAR(250) NOT NULL,
 password VARCHAR(250) NOT NULL,
 code VARCHAR(250) NOT NULL,
 ready_to_extraction BOOLEAN DEFAULT FALSE
 );

INSERT INTO machine (host,password,code) VALUES ('http://localhost:9006', 'admin-abcd' , 'maq-def');

INSERT INTO machine (host,password,code) VALUES ('http://localhost:9007', 'admin-bb' , 'maq-1');
INSERT INTO machine (host,password,code) VALUES ('http://localhost:9001', 'admin-cc' , 'maq-2');
INSERT INTO machine (host,password,code) VALUES ('http://localhost:9002', 'admin-dd' , 'maq-3');
INSERT INTO machine (host,password,code) VALUES ('http://localhost:9003', 'admin-rr' , 'maq-4');
INSERT INTO machine (host,password,code) VALUES ('http://localhost:9004', 'admin-ee' , 'maq-5');

INSERT INTO machine (host,password,code) VALUES ('http://localhost:9999', 'admin123' , 'maq-desconectada');


