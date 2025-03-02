package pl.myproject.car_rental_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.entity.User;
import pl.myproject.car_rental_api.exception.UserNotFoundException;
import pl.myproject.car_rental_api.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {

        // fetching user with roles
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + username + " not found"));

        // mapping user roles to list of GrantedAuthority type
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map( authority -> new SimpleGrantedAuthority(authority.getRole().toString()))
                .collect(Collectors.toUnmodifiableList());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
