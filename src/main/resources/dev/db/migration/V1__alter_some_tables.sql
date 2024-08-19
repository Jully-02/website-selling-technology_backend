-- versioned migrations
ALTER TABLE category MODIFY name VARCHAR(50) UNIQUE;

ALTER TABLE product MODIFY price DECIMAL(10,2);

ALTER TABLE `user` MODIFY COLUMN `phone_number` VARCHAR(15);
ALTER TABLE `user` MODIFY COLUMN `password` CHAR(100) NOT NULL;