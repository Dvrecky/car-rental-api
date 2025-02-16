package pl.myproject.car_rental_api.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.myproject.car_rental_api.dto.reservation.ReservationDTO;
import pl.myproject.car_rental_api.entity.Reservation;

@Configuration
public class ModelMapperConfig {

    @Bean(name = "defaultModelMapper")
    public ModelMapper defaultModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        return modelMapper;
    }

    @Bean(name = "toReservationDTOModelMapper")
    public ModelMapper toReservationDTOModelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.typeMap(Reservation.class, ReservationDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getCar(), ReservationDTO::setCarDTO);
        });

        return modelMapper;
    }
}
