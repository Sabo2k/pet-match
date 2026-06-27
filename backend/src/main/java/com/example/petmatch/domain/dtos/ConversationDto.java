package com.example.petmatch.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationDto {
    private UUID id;
    private List<AuthorDto> participants;
    private UUID advertisementId;
    private String advertisementTitle;
    private List<MessageDto> messages;
    private LocalDateTime createdAt;
}
