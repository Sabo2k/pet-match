package com.example.petmatch.services;

import com.example.petmatch.domain.entities.Conversation;
import com.example.petmatch.domain.entities.Message;
import com.example.petmatch.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface ConversationService {
    Conversation startConversation(User sender, UUID recipientId, UUID advertisementId, String firstMessage);
    List<Conversation> getConversationsForUser(User user);
    Conversation getConversationById(UUID id);
    Message sendMessage(UUID conversationId, User sender, String content);
}
