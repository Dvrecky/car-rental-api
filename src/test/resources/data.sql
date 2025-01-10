INSERT INTO engines (capacity, horsepower, torque, fuel_type, cylinder_configuration, engine_type)
VALUES
(2.0, 150, 300, 'Petrol', 'Inline', 'Turbocharged'),
(3.5, 250, 500, 'Diesel', 'V6', 'Naturally Aspirated'),
(1.6, 120, 200, 'Petrol', 'Inline', 'Naturally Aspirated');

INSERT INTO gearboxes (name, producer, number_of_gears, type)
VALUES
('Manual 5', 'ZF', 5, 'Manual'),
('Automatic 6', 'Aisin', 6, 'Automatic'),
('CVT', 'Jatco', 1, 'CVT');

INSERT INTO models (name, type, production_year, brand, brand_country, color, type_of_drive, number_of_doors, body_type, number_of_seats, environmental_label, fuel_consumption, CO2_emissions, weight, "0-100_time", photo_url, average_price, description, engine_id, gearbox_id)
VALUES
('Model A', 'Sedan', '2023-05-01', 'BrandA', 'Germany', 'Red', 'FWD', 4, 'Sedan', 5, 'Euro 6', '6.5 L/100km', '140 g/km', 1200, 8.3, 'https://example.com/photoA.jpg', 25000, 'A high-performance sedan with sporty handling.', 1, 2),
('Model B', 'SUV', '2021-11-15', 'BrandB', 'USA', 'Blue', 'AWD', 5, 'SUV', 7, 'Euro 5', '8.3 L/100km', '200 g/km', 1800, 10.2, 'https://example.com/photoB.jpg', 30000, 'A family SUV offering great comfort and space.', 2, 3),
('Model C', 'Hatchback', '2020-03-10', 'BrandC', 'Japan', 'Green', 'RWD', 3, 'Hatchback', 4, 'Euro 6', '5.5 L/100km', '120 g/km', 900, 7.5, 'https://example.com/photoC.jpg', 15000, 'A compact hatchback ideal for city driving.', 3, 1);

INSERT INTO cars (registration_number, last_service_date, mileage, insurance_expiry_date, rental_price_per_day, base_price, model_id)
VALUES
('ABC1234', '2024-12-01', 25000, '2025-12-01', 120.50, 22000, 1),
('XYZ5678', '2024-11-15', 18000, '2025-11-15', 150.75, 35000, 2),
('LMN9876', '2024-09-30', 12000, '2025-09-30', 90.30, 16000, 3);
