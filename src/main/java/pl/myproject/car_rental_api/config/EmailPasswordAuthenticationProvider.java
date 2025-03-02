package pl.myproject.car_rental_api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.myproject.car_rental_api.exception.UserNotFoundException;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(email);
        } catch (UserNotFoundException ex) {
            throw new BadCredentialsException("Bad credentials");
        }

        try {
            checkPasswordCorrectness(userDetails, (UsernamePasswordAuthenticationToken) authentication);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Bad credentials");
        }

        return UsernamePasswordAuthenticationToken.authenticated(email, authentication.getCredentials(), userDetails.getAuthorities());
    }

    // method checks if this provider is able to do an authentication process
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void checkPasswordCorrectness(UserDetails userDetails,
                                          UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if(authentication.getCredentials() == null) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException("Bad credentials");
        }

        String receivedPassword = authentication.getCredentials().toString();
        if(!this.passwordEncoder.matches(receivedPassword, userDetails.getPassword())) {
            log.debug("Failed to authenticate since provided password doesn't match stored value");
            throw new BadCredentialsException("Bad credentials");
        }

    }
}
