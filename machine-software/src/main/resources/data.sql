
DROP TABLE IF EXISTS item;
CREATE TABLE item (
 id INT AUTO_INCREMENT  PRIMARY KEY,
 name VARCHAR(250) NOT NULL,
 code VARCHAR(250) NOT NULL,
 price FLOAT(10) NOT NULL,
  stock INT NOT NULL
 
);

INSERT INTO item (name,code,price,stock) VALUES ('Gaseosa Pepsi', 'PEPSI-CHICA' , 75, 1);
INSERT INTO item (name,code,price,stock) VALUES ('Chicles Bazoka', 'CHICK-BAZK' , 5, 1);
INSERT INTO item (name,code,price,stock) VALUES ('Papas Fritas', 'PAPAS-FRI' , 4, 1);

INSERT INTO item (name,code,price,stock) VALUES ('Gaseosa Pepsi Grande', 'PEPSI-CHICA-GR' , 75, 4);
INSERT INTO item (name,code,price,stock) VALUES ('Chicles Bazoka Grande', 'CHICK-BAZK-GR' , 5, 3);
INSERT INTO item (name,code,price,stock) VALUES ('Papas Fritas Grande', 'PAPAS-FRI-GR' , 4, 5);

INSERT INTO item (name,code,price,stock) VALUES ('Gaseosa Pepsi Chicas', 'PEPSI-CHICA-CH' , 75, 4);
INSERT INTO item (name,code,price,stock) VALUES ('Chicles Bazoka Chicas', 'CHICK-BAZK-CH' , 5, 3);
INSERT INTO item (name,code,price,stock) VALUES ('Papas Fritas Chicas', 'PAPAS-FRI-CH' , 4, 5);

INSERT INTO item (name,code,price,stock) VALUES ('Gaseosa Pepsi Picantes', 'PEPSI-CHICA-PIC' , 75, 4);
INSERT INTO item (name,code,price,stock) VALUES ('Chicles Bazoka Picantes', 'CHICK-BAZK-PIC' , 5, 3);
INSERT INTO item (name,code,price,stock) VALUES ('Papas Fritas Picantes', 'PAPAS-FRI-PIC' , 4, 5);


INSERT INTO change (code,quantity) VALUES ('COIN_05_CENTS',3);
INSERT INTO change (code,quantity) VALUES ('COIN_10_CENTS',7);


DROP TABLE IF EXISTS cashMovement;
CREATE TABLE cashMovement (
 id INT AUTO_INCREMENT  PRIMARY KEY,
 ammount INT NOT NULL
);


INSERT INTO cashmovement (ammount) VALUES (-400);
INSERT INTO transaction (method, item, quantity, total) VALUES ('CASH', 1, 2, 50);



