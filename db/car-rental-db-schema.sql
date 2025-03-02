DROP DATABASE IF EXISTS carRentalDb;

-- CREATE DATABASE IF NOT EXISTS carRentalDb
CREATE DATABASE carRentalDb
-- ustawiony zestaw znaków utf8mb4, zalecany w nowych projektach
CHARACTER SET utf8mb4
-- traktuje znaki małe i duże tak samo
COLLATE utf8mb4_unicode_ci;

USE carRentalDb;

CREATE TABLE engines(
	id INT PRIMARY KEY AUTO_INCREMENT, -- primary key obejmuje unique oraz not null
    capacity DECIMAL(3, 1) NOT NULL,
    horsepower SMALLINT NOT NULL,
    torque SMALLINT NOT NULL,
    fuel_type VARCHAR(20) NOT NULL,
    cylinder_configuration VARCHAR(10) NOT NULL,
    engine_type VARCHAR(20) NOT NULL
);

CREATE TABLE gearboxes(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    producer VARCHAR(20) NOT NULL,
    number_of_gears TINYINT NOT NULL,
    type VARCHAR(20) NOT NULL
);

CREATE TABLE models(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL,
    type VARCHAR(20) NOT NULL,
    production_year DATE NOT NULL,
    brand VARCHAR(20) NOT NULL,
    brand_country VARCHAR(20) NOT NULL,
    color VARCHAR(30) NOT NULL,
    type_of_drive VARCHAR(3) NOT NULL,
    number_of_doors TINYINT NOT NULL,
    body_type VARCHAR(20) NOT NULL,
    number_of_seats TINYINT NOT NULL,
    environmental_label VARCHAR(20) NOT NULL,
    fuel_consumption DECIMAL(4,2) NOT NULL,
    CO2_emissions DECIMAL(5,1) NOT NULL,
    weight SMALLINT NOT NULL,
    `0-100_time` DECIMAL(4,2) NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    average_price INT NOT NULL,
    description TEXT NOT NULL,
    engine_id INT NOT NULL,
    gearbox_id INT NOT NULL,

    CONSTRAINT FK_ModelEngine FOREIGN KEY (engine_id) REFERENCES engines(id),
    CONSTRAINT FK_ModelGearboxes FOREIGN KEY (gearbox_id) REFERENCES gearboxes(id)

);

CREATE TABLE cars (
    id INT PRIMARY KEY AUTO_INCREMENT,
    registration_number VARCHAR(7) UNIQUE NOT NULL,
    vin VARCHAR(17) UNIQUE NOT NULL,
    last_service_date DATE NOT NULL,
    mileage INT NOT NULL,
    insurance_expiry_date DATE NOT NULL,
    rental_price_per_day INT NOT NULL,
    base_price INT NOT NULL,
    model_id INT NOT NULL,

    CONSTRAINT FK_CarModel FOREIGN KEY (model_id) REFERENCES models(id)
);

CREATE TABLE car_conditions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    car_id INT NOT NULL,
    name VARCHAR(20) NOT NULL,
    is_rentable BIT NOT NULL,
    description TEXT NOT NULL,

    CONSTRAINT FK_CarConditionsCar FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE car_availability (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    car_id INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,

    CONSTRAINT FK_CarAvailability FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE equipments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    category VARCHAR(30) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE users (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    e_mail VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    creation_date DATETIME NOT NULL
);

CREATE TABLE roles (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name ENUM("ROLE_CLIENT", "ROLE_EMPLOYEE", "ROLE_ADMIN")
);

CREATE TABLE user_role (
	user_id BIGINT NOT NULL,
    role_id INT NOT NULL,

    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_UserRoleUser FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT FK_UserRoleRole FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    car_id INT NOT NULL,
    user_id BIGINT NOT NULL,
    booking_date DATETIME NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    total_price INT NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    remarks TEXT,

    CONSTRAINT FK_ReservationCar FOREIGN KEY (car_id) REFERENCES cars(id),
    CONSTRAINT FK_ReservationUser FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE rents (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    rental_start_date DATETIME NOT NULL,
    rental_end_date DATETIME,
    damage_report TEXT,
    penalty_fee INT DEFAULT 0,
    update_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) NOT NULL,
    
    CONSTRAINT FK_RentReservation FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE TABLE car_equipment (
    model_id INT NOT NULL,
    equipment_id INT NOT NULL,
    
    PRIMARY KEY (model_id, equipment_id),
    CONSTRAINT FK_CarEquipmentModel FOREIGN KEY (model_id) REFERENCES models(id),
    CONSTRAINT FK_CarEquipmentEquipment FOREIGN KEY (equipment_id) REFERENCES equipments(id)
);



