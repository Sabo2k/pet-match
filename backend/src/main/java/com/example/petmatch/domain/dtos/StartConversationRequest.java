package com.example.petmatch.domain.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class StartConversationRequest {
    private UUID recipientId;
    private UUID advertisementId;
    private String message;
}
