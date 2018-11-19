DROP DATABASE IF EXISTS HBS;
CREATE DATABASE HBS;
USE HBS; 
DROP TABLE IF EXISTS ROOM;
CREATE TABLE ROOM
(
 roomNumber INT,
 hotelName VARCHAR(30),
 reserved BOOL,
 bedType VARCHAR(30),
 bedNumber INT,
 maxOccupancy INT,
 price INT,
 primary key (roomNumber, hotelName)

);
DROP TABLE IF EXISTS CUSTOMER;
CREATE TABLE CUSTOMER
(
 cID INT AUTO_INCREMENT,
 name VARCHAR(30),
 hotelName VARCHAR(30),
 age INT,
 roomNumber INT,
 PRIMARY KEY (cID)
) ;
ALTER table CUSTOMER AUTO_INCREMENT = 1001;

DROP TABLE IF EXISTS HOTEL;
CREATE TABLE HOTEL
(
 hotelName VARCHAR(30), 
 pool BOOL,
 elevators BOOL,
 freeBreakfast BOOL,
 PRIMARY KEY (hotelName)
); 

DROP TABLE IF EXISTS RESERVATION;
CREATE TABLE RESERVATION
(
 cID INT,
 hotelName VARCHAR(30),
 checkIn DATE,
 checkOut DATE,
 PRIMARY KEY(cID),
 FOREIGN KEY(cID) REFERENCES CUSTOMER(cID)
); 

DROP TABLE IF EXISTS RATING;
CREATE TABLE RATING
(
 cID INT,
 name VARCHAR(30),
 hotelName VARCHAR(30),
 rDate DATE,
 stars INT,
 PRIMARY KEY (cID),
 FOREIGN KEY(cID) REFERENCES CUSTOMER(cID)
); 

LOAD DATA LOCAL INFILE 'C:/SQL/hbs/room.txt' INTO TABLE ROOM;
LOAD DATA LOCAL INFILE 'C:/SQL/hbs/customer.txt' INTO TABLE CUSTOMER;
LOAD DATA LOCAL INFILE 'C:/SQL/hbs/hotel.txt' INTO TABLE HOTEL;
LOAD DATA LOCAL INFILE 'C:/SQL/hbs/reservation.txt' INTO TABLE RESERVATION;
LOAD DATA LOCAL INFILE 'C:/SQL/hbs/rating.txt' INTO TABLE RATING;