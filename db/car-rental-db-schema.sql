DROP DATABASE IF EXISTS carRentalDb;

CREATE DATABASE carRentalDb
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE carRentalDb;

CREATE TABLE engines(
    id INT PRIMARY KEY AUTO_INCREMENT,
    capacity DECIMAL(5, 3) NOT NULL,
    horsepower SMALLINT NOT NULL,
    torque SMALLINT NOT NULL,
    fuel_type VARCHAR(20) NOT NULL,
    cylinder_configuration VARCHAR(10) NOT NULL,
    engine_type VARCHAR(20) NOT NULL
);

CREATE TABLE gearboxes(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    producent VARCHAR(20) NOT NULL,
    number_of_gears TINYINT NOT NULL,
    type VARCHAR(20) NOT NULL
);

CREATE TABLE models(
    id INT PRIMARY KEY AUTO_INCREMENT,
    engine_id INT NOT NULL,
    gearbox_id INT NOT NULL,
    name VARCHAR(40) NOT NULL,
    type VARCHAR(20) NOT NULL,
    production_year DATE NOT NULL,
    brand VARCHAR(20) NOT NULL,
    brand_country VARCHAR(20) NOT NULL,
    color VARCHAR(30) NOT NULL,
    type_od_drive VARCHAR(3) NOT NULL,
    number_of_doors TINYINT NOT NULL,
    body_type VARCHAR(20) NOT NULL,
    number_of_seats TINYINT NOT NULL,
    environmental_label VARCHAR(20) NOT NULL,
    fuel_consumption VARCHAR(10) NOT NULL,
    C02_emissions VARCHAR(20) NOT NULL,
    weight SMALLINT NOT NULL,
    `0-100_time` DECIMAL(4,2) NOT NULL,
    photo_url VARCHAR(30) NOT NULL,
    average_price INT NOT NULL,
    description TEXT NOT NULL,
    
    CONSTRAINT FK_ModelEngine FOREIGN KEY (engine_id)
    REFERENCES engines(id),
    CONSTRAINT FK_ModelGearbox FOREIGN KEY (gearbox_id)
    REFERENCES gearboxes(id)
);

CREATE TABLE cars (
    id INT PRIMARY KEY AUTO_INCREMENT,
    registration_number VARCHAR(7) UNIQUE NOT NULL,
    last_service_date DATE NOT NULL,
    mileage VARCHAR(7) NOT NULL,
    model_id INT NOT NULL,
    insurance_expiry_date DATE NOT NULL,
    rental_price_per_day VARCHAR(30) NOT NULL,
    base_price INT NOT NULL,
    
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
    `from` DATETIME NOT NULL,
    `to` DATETIME NOT NULL,
    
    CONSTRAINT FK_CarAvailability FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE equipments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    category VARCHAR(30) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    car_id INT NOT NULL,
    reservation_booking_date DATETIME NOT NULL,
    reservation_start_date DATETIME NOT NULL,
    reservation_end_date DATETIME NOT NULL,
    total_price INT NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    reservation_status VARCHAR(20) NOT NULL,
    remarks TEXT,
    
    CONSTRAINT FK_ReservationCar FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE rents (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    rental_start_date DATETIME NOT NULL,
    rental_end_date DATETIME NOT NULL,
    damage_report TEXT,
    penalty_fee INT,
    update_date DATETIME NOT NULL,
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
CREATE TABLE model_engine (
    model_id INT NOT NULL,
    engine_id INT NOT NULL,
    
    PRIMARY KEY (model_id, engine_id),
    CONSTRAINT FK_ModelEngineModel FOREIGN KEY (model_id) REFERENCES models(id),
    CONSTRAINT FK_ModelEngineEngine FOREIGN KEY (engine_id) REFERENCES engines(id)
);

CREATE TABLE model_gearbox (
    model_id INT NOT NULL,
    gearbox_id INT NOT NULL,
    
    PRIMARY KEY (model_id, gearbox_id),
    CONSTRAINT FK_ModelGearboxModel FOREIGN KEY (model_id) REFERENCES models(id),
    CONSTRAINT FK_ModelGearboxGearbox FOREIGN KEY (gearbox_id) REFERENCES gearboxes(id)
);


