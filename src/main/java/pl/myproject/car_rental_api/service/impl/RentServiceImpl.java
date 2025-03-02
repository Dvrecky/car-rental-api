package pl.myproject.car_rental_api.service.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.rent.AddRentDTO;
import pl.myproject.car_rental_api.dto.rent.RentDTO;
import pl.myproject.car_rental_api.dto.rent.UpdateRentDTO;
import pl.myproject.car_rental_api.entity.Rent;
import pl.myproject.car_rental_api.projection.ClientRentBaseView;
import pl.myproject.car_rental_api.repository.RentRepository;
import pl.myproject.car_rental_api.service.RentService;
import pl.myproject.car_rental_api.service.ReservationService;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional
    @Override
    public RentDTO updateRent(UpdateRentDTO updateRentDTO) {

        long rentId = updateRentDTO.getId();
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow( () -> new NoSuchElementException("Rent for ID: " + rentId + " not found"));

        rent.setEndDate(updateRentDTO.getEndDate());
        rent.setUpdateDate(updateRentDTO.getUpdateDate());
        rent.setStatus(updateRentDTO.getStatus());

        return modelMapper.map(rent, RentDTO.class);
    }

    @Override
    public List<ClientRentBaseView> getRentsByClientId(long clientId) {
        return rentRepository.findAllRentsBaseView(clientId);
    }
}
