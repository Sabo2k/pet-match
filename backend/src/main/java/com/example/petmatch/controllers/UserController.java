package com.example.petmatch.controllers;

import com.example.petmatch.domain.entities.User;
import com.example.petmatch.security.AdvertisementUserDetails;
import com.example.petmatch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me/saved-advertisements")
    public ResponseEntity<List<UUID>> getSavedAdvertisementIds() {
        return ResponseEntity.ok(userService.getSavedAdvertisementIds(getCurrentUser()));
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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdvertisementUserDetails userDetails = (AdvertisementUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
