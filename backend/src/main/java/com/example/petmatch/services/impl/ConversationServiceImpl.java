package com.example.petmatch.services.impl;

import com.example.petmatch.domain.entities.Advertisement;
import com.example.petmatch.domain.entities.Conversation;
import com.example.petmatch.domain.entities.Message;
import com.example.petmatch.domain.entities.User;
import com.example.petmatch.repositories.AdvertisementRepository;
import com.example.petmatch.repositories.ConversationRepository;
import com.example.petmatch.repositories.MessageRepository;
import com.example.petmatch.repositories.UserRepository;
import com.example.petmatch.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    @Override
    @Transactional
    public Conversation startConversation(User sender, UUID recipientId, UUID advertisementId, String firstMessage) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));

        Optional<Conversation> existing = conversationRepository
                .findByParticipantsAndAdvertisement(sender.getId(), recipientId, advertisementId);

        Conversation conversation = existing.orElseGet(() -> {
            Conversation newConversation = Conversation.builder()
                    .participants(new LinkedHashSet<>(List.of(sender, recipient)))
                    .advertisement(advertisement)
                    .build();
            return conversationRepository.save(newConversation);
        });

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(firstMessage)
                .build();
        messageRepository.save(message);

        return conversationRepository.findByIdWithDetails(conversation.getId())
                .orElseThrow();
    }

    @Override
    public List<Conversation> getConversationsForUser(User user) {
        return conversationRepository.findByParticipantId(user.getId());
    }

    @Override
    public Conversation getConversationById(UUID id) {
        return conversationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    @Override
    @Transactional
    public Message sendMessage(UUID conversationId, User sender, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(p -> p.getId().equals(sender.getId()));
        if (!isParticipant) {
            throw new RuntimeException("User is not a participant of this conversation");
        }

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(content)
                .build();
        return messageRepository.save(message);
    }
}
