package com.example.petmatch.controllers;

import com.example.petmatch.domain.dtos.AdvertisementDto;
import com.example.petmatch.domain.dtos.AuthorDto;
import com.example.petmatch.domain.dtos.ChangeEmailRequest;
import com.example.petmatch.domain.dtos.ChangePasswordRequest;
import com.example.petmatch.domain.dtos.ImageDto;
import com.example.petmatch.domain.dtos.UserProfileDto;
import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.security.AdvertisementUserDetails;
import com.example.petmatch.services.AuthenticationService;
import com.example.petmatch.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getProfile() {
        return ResponseEntity.ok(userService.getProfile(getCurrentUser()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getProfileById(userId));
    }

    @GetMapping("/{userId}/advertisements")
    public ResponseEntity<List<AdvertisementDto>> getUserAdvertisements(@PathVariable UUID userId) {
        List<Advertisement> ads = userService.getAdvertisementsByUserId(userId);
        return ResponseEntity.ok(ads.stream().map(this::convertToDto).toList());
    }

    @GetMapping("/me/created-advertisements")
    public ResponseEntity<List<AdvertisementDto>> getCreatedAdvertisements() {
        List<Advertisement> ads = userService.getCreatedAdvertisements(getCurrentUser());
        return ResponseEntity.ok(ads.stream().map(this::convertToDto).toList());
    }

    @GetMapping("/me/saved-advertisements")
    public ResponseEntity<List<UUID>> getSavedAdvertisementIds() {
        return ResponseEntity.ok(userService.getSavedAdvertisementIds(getCurrentUser()));
    }

    @GetMapping("/me/saved-advertisements/details")
    public ResponseEntity<List<AdvertisementDto>> getSavedAdvertisementsDetails() {
        List<Advertisement> ads = userService.getSavedAdvertisementDetails(getCurrentUser());
        return ResponseEntity.ok(ads.stream().map(this::convertToDto).toList());
    }

    @PostMapping("/me/saved-advertisements/{advertisementId}")
    public ResponseEntity<Void> saveAdvertisement(@PathVariable UUID advertisementId) {
        userService.saveAdvertisement(getCurrentUser(), advertisementId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/saved-advertisements/{advertisementId}")
    public ResponseEntity<Void> unsaveAdvertisement(@PathVariable UUID advertisementId) {
        userService.unsaveAdvertisement(getCurrentUser(), advertisementId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(HttpServletResponse response) {
        userService.deleteAccount(getCurrentUser());
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(getCurrentUser(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/email")
    public ResponseEntity<Void> changeEmail(@RequestBody ChangeEmailRequest request, HttpServletResponse response) {
        userService.changeEmail(getCurrentUser(), request.getNewEmail());
        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(request.getNewEmail());
        String newToken = authenticationService.generateToken(updatedUserDetails);
        Cookie cookie = new Cookie("authToken", newToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    private AdvertisementDto convertToDto(Advertisement advertisement) {
        AuthorDto authorDto = null;
        if (advertisement.getAuthor() != null) {
            authorDto = AuthorDto.builder()
                    .id(advertisement.getAuthor().getId())
                    .username(advertisement.getAuthor().getUsername())
                    .build();
        }

        List<ImageDto> imageDtos = advertisement.getImages().stream()
                .map(image -> ImageDto.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .isPrimary(image.isPrimary())
                        .createdAt(image.getCreatedAt())
                        .build())
                .toList();

        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .description(advertisement.getDescription())
                .age(advertisement.getAge())
                .price(advertisement.getPrice())
                .location(advertisement.getLocation())
                .author(authorDto)
                .images(imageDtos)
                .createdAt(advertisement.getCreatedAt())
                .updatedAt(advertisement.getUpdatedAt())
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdvertisementUserDetails userDetails = (AdvertisementUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
