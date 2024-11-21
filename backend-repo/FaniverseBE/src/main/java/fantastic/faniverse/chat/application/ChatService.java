package fantastic.faniverse.chat.application;

import fantastic.faniverse.chat.domain.ChatMessage;
import fantastic.faniverse.chat.domain.ChatRoom;
import fantastic.faniverse.chat.infrastructure.repository.ChatMessageRepository;
import fantastic.faniverse.chat.infrastructure.repository.ChatRoomRepository;
import fantastic.faniverse.chat.presentation.request.MessageRequest;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void createMessage(Long userId, Long roomId, MessageRequest messageRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        ChatMessage chatMessage = messageRequest.toEntity(user, chatRoom);
        chatMessageRepository.save(chatMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findLastMessages(Long roomId, Long lastMessageId) {
        return chatMessageRepository.findByChatRoomIdAndIdGreaterThan(roomId, lastMessageId);
    }
}

