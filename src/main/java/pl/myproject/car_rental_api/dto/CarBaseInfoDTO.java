package pl.myproject.car_rental_api.dto;

import lombok.Getter;

import java.math.BigDecimal;

//@Getter
//public class CarBaseInfoDTO {
//
//    private int id;
//    private int rentalPricePerDay;
//    private String fullName;
//    private String typeOfDrive;
//    private int numberOfSeats;
//    private BigDecimal accelerationTime;
//    private BigDecimal capacity;
//    private int horsepower;
//    private int torque;
//    private String cylinderConfiguration;
//    private int numberOfGears;
//}

public record CarBaseInfoDTO(int id, int rentalPricePerDay, String fullName, String typeOfDrive, int numberOfSeats,
                             BigDecimal accelerationTime, BigDecimal capacity, int horsepower, int torque,
                             String cylinderConfiguration, int numberOfGears)
{}
