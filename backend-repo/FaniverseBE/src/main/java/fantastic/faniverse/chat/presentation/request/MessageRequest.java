package fantastic.faniverse.chat.presentation.request;

import fantastic.faniverse.chat.domain.ChatMessage;
import fantastic.faniverse.chat.domain.ChatRoom;
import fantastic.faniverse.chat.domain.MessageType;
import fantastic.faniverse.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageRequest {
    private Long userId;
    private String message;
    private String imageUrl;

    public ChatMessage toEntity(User sender, ChatRoom chatRoom) {
        ChatMessage.ChatMessageBuilder builder = ChatMessage.builder()
                .createdAt(LocalDateTime.now())
                .chatRoom(chatRoom)
                .senderId(sender.getId());

        if (imageUrl == null) {
            return builder.message(message)
                    .messageType(MessageType.TEXT)
                    .build();
        }
        return builder
                .imageLink(imageUrl)
                .messageType(MessageType.IMAGE)
                .build();
    }
}