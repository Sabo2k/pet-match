package com.example.petmatch.repositories;

import com.example.petmatch.domain.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("SELECT DISTINCT c FROM Conversation c " +
           "LEFT JOIN FETCH c.participants " +
           "LEFT JOIN FETCH c.messages m " +
           "LEFT JOIN FETCH m.sender " +
           "WHERE c.id = :id")
    Optional<Conversation> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT DISTINCT c FROM Conversation c " +
           "LEFT JOIN FETCH c.participants p " +
           "WHERE p.id = :userId")
    List<Conversation> findByParticipantId(@Param("userId") UUID userId);

    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE p1.id = :userId1 AND p2.id = :userId2 " +
           "AND c.advertisement IS NOT NULL AND c.advertisement.id = :advertisementId")
    Optional<Conversation> findByParticipantsAndAdvertisement(
            @Param("userId1") UUID userId1,
            @Param("userId2") UUID userId2,
            @Param("advertisementId") UUID advertisementId);
}
