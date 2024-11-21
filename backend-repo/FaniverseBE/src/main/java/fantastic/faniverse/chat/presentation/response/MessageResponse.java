package fantastic.faniverse.chat.presentation.response;

import fantastic.faniverse.chat.domain.ChatMessage;
import fantastic.faniverse.chat.domain.MessageType;
import lombok.Getter;

public record MessageResponse(Long id, String message, String imageUrl, MessageType messageType, Long senderId) {
    public static MessageResponse of(ChatMessage chatMessage) {
        return new MessageResponse(
                chatMessage.getId(),
                chatMessage.getMessage(),
                chatMessage.getImageLink(),
                chatMessage.getMessageType(),
                chatMessage.getSenderId()
        );
    }
}
