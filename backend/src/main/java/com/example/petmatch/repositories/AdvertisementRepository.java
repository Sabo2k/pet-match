package com.example.petmatch.repositories;

import com.example.petmatch.domain.entities.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, UUID> {
    List<Advertisement> findAll();
    List<Advertisement> findByAuthor_Id(UUID authorId);
}
