package com.example.petmatch.security;

import com.example.petmatch.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that intercepts incoming HTTP requests to validate JWT tokens and set the authentication context.
 * This filter is executed once per request (hence it extends OncePerRequestFilter) and checks for the presence of a JWT
 * token in the Authorization header.
 * If a valid token is found, it retrieves the user details and sets the authentication in the SecurityContext, allowing
 * the user to be authenticated for the duration of the request.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Service responsible for handling authentication logic, including validating JWT tokens and retrieving user
     * details. This service is injected into the filter using constructor injection, allowing the filter to delegate
     * token validation and user retrieval tasks to it. The AuthenticationService is expected to contain the logic for
     * validating JWT tokens, such as checking the token's signature, expiration, and extracting user information from
     * the token claims to load the corresponding user details for authentication purposes.
     */
    private final AuthenticationService authenticationService;

    /**
     * Filters incoming HTTP requests to validate JWT tokens from cookies and set the authentication context
     * if a valid token is found.
     * @param request the incoming HTTP request that may contain a JWT token in an httpOnly cookie, which will be
     *                extracted and validated by the filter to authenticate the user for the request.
     * @param response the HTTP response that can be modified by the filter if needed, although in this implementation
     *                 it is not used
     * @param filterChain the filter chain that allows the request to proceed to the next filter or endpoint after
     *                    processing the JWT token, ensuring that the request is properly authenticated before reaching
     *                    the protected resources.
     * @throws ServletException if an error occurs during the filtering process, although in this implementation,
     *                          exceptions are caught and logged
     * @throws IOException if an I/O error occurs during the filtering process, although in this implementation,
     *                     exceptions are caught and logged
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extract the JWT token from the httpOnly cookie in the incoming request.
            // The token is stored in a cookie named "authToken" for security and XSS protection.
            String token = extractTokenFromCookie(request);

            if(token != null) {
                // Validate the token using the AuthenticationService.
                // If the token is valid, it will return the UserDetails
                UserDetails userDetails = authenticationService.validateToken(token);

                // Create an authentication token using the retrieved UserDetails and set it in the SecurityContext.
                // This allows the user to be authenticated for the duration of the request, enabling access to
                // protected resources based on the user's roles and permissions defined in the UserDetails.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Set the authentication in the SecurityContext, allowing the user to be authenticated for the duration
                // of the request.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // If the UserDetails is an instance of AdvertisementUserDetails, set the userId as a request attribute
                // for use in controllers or other components that may need to access the authenticated user's ID during
                // the request processing.
                if(userDetails instanceof AdvertisementUserDetails) {
                    request.setAttribute("userId", ((AdvertisementUserDetails) userDetails).getId());
                }
            }
        }
        catch(Exception exception) {
            // Do not throw exceptions, just don't authenticate the user
            log.warn("Received invalid auth token");
        }
        // Continue the filter chain to allow the request to proceed to the next filter or endpoint, regardless of
        // whether authentication was successful or not, ensuring that unauthenticated requests can still reach public
        // endpoints while authenticated
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the httpOnly authentication cookie in the incoming HTTP request.
     * The method iterates through all cookies to find the one named "authToken". If found, it returns the cookie value.
     * If the cookie is not found, the method returns null, allowing the filter to determine whether to attempt
     * authentication based on the presence of a token.
     * @param request the incoming HTTP request that may contain the httpOnly "authToken" cookie with the JWT token
     * @return the extracted JWT token if the "authToken" cookie is found, or null if the cookie is not present or if
     *         the request contains no cookies, allowing the filter to determine whether to attempt authentication based
     *         on the presence of a token.
     */
    private String extractTokenFromCookie(HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(jakarta.servlet.http.Cookie cookie : cookies) {
                if("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
