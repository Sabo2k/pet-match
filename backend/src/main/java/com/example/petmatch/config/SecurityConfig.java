package com.example.petmatch.config;

import com.example.petmatch.domain.entities.User;
import com.example.petmatch.security.AdvertisementUserDetailsService;
import com.example.petmatch.security.JwtAuthenticationFilter;
import com.example.petmatch.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.petmatch.repositories.UserRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration class for Spring Security settings in the PetMatch application.
 * This class defines beans for authentication, authorization, CORS and JWT.
 * It ensures access to endpoints and manages user sessions statelessly.
 */
@Configuration
public class SecurityConfig {

    /**
     * This filter validates JWT tokens for incoming requests and sets the authentication in the context.
     * @param authenticationService The service used to authenticate users and validate tokens
     * @return a configured JwtAuthenticationFilter instance.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) {
        // Create and return a new instance of the JwtAuthenticationFilter, passing the authentication service to its
        // constructor, so it can use it for validating JWT tokens and authenticating users.
        return new JwtAuthenticationFilter(authenticationService);
    }

    /**
     * Defines a UserDetailsService bean that loads user-specific data for authentication.
     * It also creates a default test user if it doesn't already exist in the database.
     * The bean annotation allows Spring to manage this service and inject it where needed for authentication processes.
     * @param userRepository The repository used to access user data from the database, required for loading user
     *                       details and creating a default user.
     * @return a UserDetailsService instance that can be used by Spring Security to authenticate users based on their
     *         email and password.
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // Create an instance of the custom UserDetailsService implementation
        AdvertisementUserDetailsService blogUserDetailsService = new AdvertisementUserDetailsService(userRepository);

        // Create a default test user if it doesn't exist
        String email = "user@test.com";
        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .username("Test User")
                    .email(email)
                    .password(passwordEncoder().encode("password"))
                    .build();
            return userRepository.save(newUser);
        });

        return blogUserDetailsService;
    }

    /**
     * Configures the security filter chain for HTTP requests.
     * Defines which endpoints are public and which require authentication.
     * @param httpSecurity HttpSecurity object to configure.
     * @param jwtAuthenticationFilter JWT filter to add to the filter chain.
     * @return a SecurityFilterChain instance with the configured security settings.
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/advertisements").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/advertisements/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/advertisements").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings.
     * Allows requests from the frontend origin (e.g., React/Vue app) and
     * specifies allowed methods and headers.
     * @return a CorsConfigurationSource instance with the configured CORS settings
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Defines a PasswordEncoder bean that uses a delegating password encoder.
     * @return a PasswordEncoder instance that can handle multiple encoding formats, including bcrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
