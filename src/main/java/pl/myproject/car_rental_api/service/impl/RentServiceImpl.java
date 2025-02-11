package pl.myproject.car_rental_api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.AddRentDTO;
import pl.myproject.car_rental_api.entity.Rent;
import pl.myproject.car_rental_api.repository.RentRepository;
import pl.myproject.car_rental_api.service.RentService;
import pl.myproject.car_rental_api.service.ReservationService;

@Service
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final ModelMapper modelMapper;
    private final ReservationService reservationService;

    public RentServiceImpl(RentRepository rentRepository, @Qualifier("defaultModelMapper") ModelMapper modelMapper, ReservationService reservationService) {
        this.rentRepository = rentRepository;
        this.modelMapper = modelMapper;
        this.reservationService = reservationService;
    }

    @Override
    public AddRentDTO addRent(AddRentDTO addRentDTO) {

        // checking if given reservation has a CONFIRMED status
        reservationService.isReservationConfirmed(addRentDTO.getReservationId());

        // saving new rent do db
        Rent rent = rentRepository.save(modelMapper.map(addRentDTO, Rent.class));

        return modelMapper.map(rent, AddRentDTO.class);
    }
}
