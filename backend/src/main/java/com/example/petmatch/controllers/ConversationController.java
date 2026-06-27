package com.example.petmatch.controllers;

import com.example.petmatch.domain.dtos.*;
import com.example.petmatch.domain.entities.Conversation;
import com.example.petmatch.domain.entities.Message;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.security.AdvertisementUserDetails;
import com.example.petmatch.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ConversationDto> startConversation(@RequestBody StartConversationRequest request) {
        User currentUser = getCurrentUser();
        Conversation conversation = conversationService.startConversation(
                currentUser,
                request.getRecipientId(),
                request.getAdvertisementId(),
                request.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDtoWithoutMessages(conversation));
    }

    @GetMapping
    public ResponseEntity<List<ConversationDto>> getMyConversations() {
        User currentUser = getCurrentUser();
        List<ConversationDto> dtos = conversationService.getConversationsForUser(currentUser)
                .stream()
                .map(this::toDtoWithoutMessages)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> getConversation(@PathVariable UUID id) {
        Conversation conversation = conversationService.getConversationById(id);
        return ResponseEntity.ok(toDto(conversation));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageDto> sendMessage(@PathVariable UUID id, @RequestBody SendMessageRequest request) {
        User currentUser = getCurrentUser();
        Message message = conversationService.sendMessage(id, currentUser, request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(toMessageDto(message));
    }

    private ConversationDto toDto(Conversation conversation) {
        List<MessageDto> messageDtos = conversation.getMessages().stream()
                .map(this::toMessageDto)
                .toList();
        return buildConversationDto(conversation, messageDtos);
    }

    private ConversationDto toDtoWithoutMessages(Conversation conversation) {
        return buildConversationDto(conversation, List.of());
    }

    private ConversationDto buildConversationDto(Conversation conversation, List<MessageDto> messages) {
        List<AuthorDto> participantDtos = conversation.getParticipants().stream()
                .map(p -> AuthorDto.builder()
                        .id(p.getId())
                        .username(p.getUsername())
                        .build())
                .toList();

        return ConversationDto.builder()
                .id(conversation.getId())
                .participants(participantDtos)
                .advertisementId(conversation.getAdvertisement() != null ? conversation.getAdvertisement().getId() : null)
                .advertisementTitle(conversation.getAdvertisement() != null ? conversation.getAdvertisement().getTitle() : null)
                .messages(messages)
                .createdAt(conversation.getCreatedAt())
                .build();
    }

    private MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdvertisementUserDetails userDetails = (AdvertisementUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
