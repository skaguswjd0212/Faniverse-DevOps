package fantastic.faniverse.chat.presentation.response;

import fantastic.faniverse.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class CharRoomResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final Long buyerId;
    private final String buyerNickname;

    public static CharRoomResponse of(ChatRoom chatRoom) {
        return new CharRoomResponse(
                chatRoom.getId(),
                chatRoom.getCreatedAt(),
                chatRoom.getUser().getId(),
                chatRoom.getUser().getUsername()
        );
    }
}
