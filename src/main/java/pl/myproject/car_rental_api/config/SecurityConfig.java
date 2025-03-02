package pl.myproject.car_rental_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // backend won't maintain user session, backend will be stateless
        http.sessionManagement( sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // securing endpoint
        .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/cars/list-view").hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/cars/*/details")).hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/cars").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/cars/**").hasRole("ADMIN")
                .requestMatchers("/api/cars/details").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.PUT, "/api/cars").hasRole("EMPLOYEE")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/cars/*/overview")).permitAll()
                .requestMatchers("/api/cars/base-info").permitAll()

                .requestMatchers("/api/reservations/client/**").hasAnyRole("CLIENT", "EMPLOYEE")
                .requestMatchers(HttpMethod.POST, "/api/reservations").hasRole("CLIENT")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/reservations/*/period")).hasRole("CLIENT")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/reservations/*/cancel")).hasAnyRole("CLIENT", "EMPLOYEE")

                .requestMatchers(HttpMethod.GET, "/api/rents").hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/rents").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET,"/api/rents/client/**").hasAnyRole("CLIENT", "EMPLOYEE")
                .requestMatchers(HttpMethod.PATCH,"/api/rents/**").hasRole("EMPLOYEE")

                .requestMatchers("/api/car-availability/**").hasAnyRole("CLIENT", "EMPLOYEE")

                .requestMatchers(HttpMethod.POST, "/api/clients/register").permitAll()

        );



        // disabling formLogin
        http.formLogin(AbstractHttpConfigurer::disable);
        // disabling cors and csrf
        http.cors(AbstractHttpConfigurer::disable);
//        http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(csrgConf -> csrgConf.disable());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker passwordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {

        EmailPasswordAuthenticationProvider authenticationProvider =
                new EmailPasswordAuthenticationProvider(userDetailsService, passwordEncoder);

        ProviderManager providerManager = new ProviderManager(authenticationProvider);

        return providerManager;
    }
}
