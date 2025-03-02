package pl.myproject.car_rental_api.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.user.AddUserDTO;
import pl.myproject.car_rental_api.dto.user.UserDTO;
import pl.myproject.car_rental_api.entity.Role;
import pl.myproject.car_rental_api.entity.User;
import pl.myproject.car_rental_api.enums.UserRole;
import pl.myproject.car_rental_api.exception.RoleNotFoundException;
import pl.myproject.car_rental_api.repository.RoleRepository;
import pl.myproject.car_rental_api.repository.UserRepository;
import pl.myproject.car_rental_api.service.EmployeeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDTO addUser(AddUserDTO addUserDTO) {

        User user = modelMapper.map(addUserDTO, User.class);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreationDate(LocalDateTime.now());

        Role clientRole = roleRepository.findByRole(UserRole.ROLE_ADMIN)
                .orElseThrow( () -> new RoleNotFoundException("Role " + UserRole.ROLE_ADMIN + " not found"));
        user.setRoles(List.of(clientRole));

        User createdUser = userRepository.save(user);

        return modelMapper.map(createdUser, UserDTO.class);
    }
}
