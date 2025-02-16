package pl.myproject.car_rental_api.dto.car;

import pl.myproject.car_rental_api.dto.model.ModelSummaryInfoDTO;

public record CarSummaryInfoDTO(
        int id,
        int rentalPricePerDay,
        ModelSummaryInfoDTO model
) {

}
