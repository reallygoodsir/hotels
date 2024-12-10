-- Create the schema
CREATE SCHEMA IF NOT EXISTS hotels_db;
USE hotels_db;

-- Create the tables

-- country table
CREATE TABLE country (
    country_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- city table
CREATE TABLE city (
    city_id INT AUTO_INCREMENT PRIMARY KEY,
    country_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (country_id) REFERENCES country(country_id)
);

-- street table
CREATE TABLE street (
    street_id INT AUTO_INCREMENT PRIMARY KEY,
    city_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (city_id) REFERENCES city(city_id)
);

-- customer table
CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(15) NOT NULL
);

-- hotel table
CREATE TABLE hotel (
    hotel_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- hotel_info table
CREATE TABLE hotel_info (
    hotel_info_id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    has_parking BOOLEAN DEFAULT FALSE,
    has_wifi BOOLEAN DEFAULT FALSE,
    has_swimming_pool BOOLEAN DEFAULT FALSE,
    details TEXT,
    FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id)
);

-- hotel_address table
CREATE TABLE hotel_address (
    hotel_address_id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    country_id INT NOT NULL,
    city_id INT NOT NULL,
    street_id INT NOT NULL,
    FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id),
    FOREIGN KEY (country_id) REFERENCES country(country_id),
    FOREIGN KEY (city_id) REFERENCES city(city_id),
    FOREIGN KEY (street_id) REFERENCES street(street_id)
);

-- room table
CREATE TABLE room (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id)
);

-- room_info table
CREATE TABLE room_info (
    room_info_id INT AUTO_INCREMENT PRIMARY KEY,
    room_id INT NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    adults_capacity INT NOT NULL,
    children_capacity INT NOT NULL,
    has_air_conditioning BOOLEAN DEFAULT FALSE,
    details TEXT,
    FOREIGN KEY (room_id) REFERENCES room(room_id)
);

-- transaction table
CREATE TABLE transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    room_id INT NOT NULL,
    customer_id INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id),
    FOREIGN KEY (room_id) REFERENCES room(room_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);
