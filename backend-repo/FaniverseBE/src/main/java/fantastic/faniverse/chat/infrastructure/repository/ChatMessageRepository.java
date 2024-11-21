package fantastic.faniverse.chat.infrastructure.repository;

import fantastic.faniverse.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :roomId AND cm.id > :lastMessageId")
    List<ChatMessage> findLastMessages(Long roomId, Long lastMessageId);

    // 'chatRoom'의 'id'를 사용하여 쿼리 작성
    List<ChatMessage> findByChatRoomIdAndIdGreaterThan(Long roomId, Long lastMessageId);
}
