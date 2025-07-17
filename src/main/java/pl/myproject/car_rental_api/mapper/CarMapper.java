package pl.myproject.car_rental_api.mapper;

import pl.myproject.car_rental_api.dto.car.CarAdminDTO;
import pl.myproject.car_rental_api.dto.engine.EngineAdminDTO;
import pl.myproject.car_rental_api.dto.gearbox.GearboxAdminDTO;
import pl.myproject.car_rental_api.dto.model.ModelAdminDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.Engine;
import pl.myproject.car_rental_api.entity.Gearbox;
import pl.myproject.car_rental_api.entity.Model;

public class CarMapper {

    private CarMapper(){}

    public static CarAdminDTO toDto(Car car) {
        Model model = car.getModel();
        Engine engine = model.getEngine();
        Gearbox gearbox = model.getGearbox();
        return CarAdminDTO.builder()
                .id(car.getId())
                .registrationNumber(car.getRegistrationNumber())
                .vin(car.getVin())
                .lastServiceDate(car.getLastServiceDate())
                .mileage(car.getMileage())
                .insuranceExpiryDate(car.getInsuranceExpiryDate())
                .rentalPricePerDay(car.getRentalPricePerDay())
                .basePrice(car.getBasePrice())
                .modelAdminDTO(ModelAdminDTO.builder()
                                .id(model.getId())
                                .name(model.getName())
                                .type(model.getType())
                                .productionYear(model.getProductionYear())
                                .brand(model.getBrand())
                                .brandCountry(model.getBrandCountry())
                                .color(model.getColor())
                                .typeOfDrive(model.getTypeOfDrive())
                                .numberOfDoors(model.getNumberOfDoors())
                                .bodyType(model.getBodyType())
                                .numberOfSeats(model.getNumberOfSeats())
                                .environmentalLabel(model.getEnvironmentalLabel())
                                .fuelConsumption(model.getFuelConsumption())
                                .CO2Emissions(model.getCO2Emissions())
                                .weight(model.getWeight())
                                .accelerationTime(model.getAccelerationTime())
                                .photoUrl(model.getPhotoUrl())
                                .averagePrice(model.getAveragePrice())
                                .description(model.getDescription())
                                .engineAdminDTO(EngineAdminDTO.builder()
                                        .id(engine.getId())
                                        .capacity(engine.getCapacity())
                                        .horsepower(engine.getHorsepower())
                                        .torque(engine.getTorque())
                                        .fuelType(engine.getFuelType())
                                        .cylinderConfiguration(engine.getCylinderConfiguration())
                                        .engineType(engine.getEngineType())
                                        .build())
                                .gearboxAdminDTO(GearboxAdminDTO.builder()
                                        .id(gearbox.getId())
                                        .name(gearbox.getName())
                                        .producer(gearbox.getProducer())
                                        .numberOfGears(gearbox.getNumberOfGears())
                                        .type(gearbox.getType())
                                        .build())
                                .build()
                ).build();
    }
}
