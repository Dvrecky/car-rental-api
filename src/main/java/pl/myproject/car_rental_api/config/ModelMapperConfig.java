package pl.myproject.car_rental_api.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.myproject.car_rental_api.dto.reservation.ReservationDTO;
import pl.myproject.car_rental_api.entity.Reservation;

@Configuration
public class ModelMapperConfig {

    @Primary
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

        TypeMap<Reservation, ReservationDTO> propertyMapper = modelMapper.createTypeMap(Reservation.class, ReservationDTO.class);
        propertyMapper.addMappings(mapper -> {
            mapper.map(src -> src.getCar().getId(), ReservationDTO::setCarId);
            mapper.map(src -> src.getUser().getId(), ReservationDTO::setUserId);
        });

        return modelMapper;
    }
}
