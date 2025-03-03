package pl.myproject.car_rental_api.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.constant.ApplicationConstants;
import pl.myproject.car_rental_api.dto.login.LoginRequest;
import pl.myproject.car_rental_api.dto.login.LoginResponse;
import pl.myproject.car_rental_api.dto.user.AddUserDTO;
import pl.myproject.car_rental_api.dto.user.UserDTO;
import pl.myproject.car_rental_api.entity.Role;
import pl.myproject.car_rental_api.entity.User;
import pl.myproject.car_rental_api.enums.UserRole;
import pl.myproject.car_rental_api.exception.RoleNotFoundException;
import pl.myproject.car_rental_api.repository.RoleRepository;
import pl.myproject.car_rental_api.repository.UserRepository;
import pl.myproject.car_rental_api.service.ClientService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final Environment environment;

    @Override
    @Transactional
    public UserDTO addUser(AddUserDTO addUserDTO) {

        User user = modelMapper.map(addUserDTO, User.class);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreationDate(LocalDateTime.now());

        Role clientRole = roleRepository.findByRole(UserRole.ROLE_CLIENT)
                .orElseThrow( () -> new RoleNotFoundException("Role " + UserRole.ROLE_CLIENT + " not found"));
        user.setRoles(List.of(clientRole));

        User createdUser = userRepository.save(user);

        return modelMapper.map(createdUser, UserDTO.class);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        String jwt = "";
        // creating authentication object (unauthenticated)
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        // starting authentication process for created Authentication
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if(authenticationResponse != null && authenticationResponse.isAuthenticated()) {
            if(environment != null) {
                // getting a secret value
                // if environment variable won't be specified, default value will be used
                String secret = environment.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                jwt = Jwts.builder()
                        .issuer("Car Rental")
                        .subject("User: " + authenticationResponse.getName())
                        .claim("Email", authenticationResponse.getName())
                        .claim("authorities", authenticationResponse.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(",")))
                        .issuedAt(new Date())
                        .expiration(new Date((new Date().getTime()) + (60*60*100)))
                        .signWith(secretKey)
                        .compact();
            }
        }

        return new LoginResponse(HttpStatus.OK.getReasonPhrase(), jwt);
    }
}
