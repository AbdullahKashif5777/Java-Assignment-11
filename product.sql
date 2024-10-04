create database product_Info;
use  product_Info;
CREATE TABLE product (
    id INT PRIMARY KEY NOT NULL, 
    name VARCHAR(50) NOT NULL,
    category VARCHAR(50),
	price DECIMAL(10, 2) Not Null Check(price>=0)
);


