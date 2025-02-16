package pl.myproject.car_rental_api.dto.gearbox;

public record GearboxSummaryInfoDTO(
        String name,
        String producer,
        int numberOfGears,
        String type
){}
