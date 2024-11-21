package fantastic.faniverse.chat.application;

import fantastic.faniverse.chat.domain.ChatRoom;
import fantastic.faniverse.chat.infrastructure.repository.ChatRoomRepository;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.repository.ProductRepository;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ChatRoom> findChatRoomByUser(User user) {
        return chatRoomRepository.findWithUserByUserAndIsDeletedNot(user);
    }

    @Transactional(readOnly = true)
    public Optional<ChatRoom> findChatRoomById(Long id) {
        return chatRoomRepository.findById(id);
    }

    @Transactional
    public ChatRoom checkChatRoomPermissions(User user, Long id) {
        ChatRoom chatRoom = chatRoomRepository
                .findByUserAndId(user, id)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        if (chatRoom.isBlocked()) {
            throw new IllegalArgumentException("차단된 채팅 방입니다.");
        }
        return chatRoom;
    }

    @Transactional
    public void deleteChatRoom(Long roomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findByUserAndId(user, roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        chatRoom.delete();
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void blockChatRoom(Long roomId, User user) {
        ChatRoom chatRoom = chatRoomRepository
                .findByUserAndId(user, roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        chatRoom.updateBlock();
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void createChatRoom(User user, Long sellerId, Long productId) {
        // 판매자 조회
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자가 존재하지 않습니다."));

        // 제품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("제품이 존재하지 않습니다."));

        // 채팅방 생성 및 저장
        ChatRoom chatRoom = ChatRoom.create(user, seller, product);
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public ChatRoom findChatRoomById(Long roomId, User user, Long productId) {
        // 채팅방을 roomId로 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);

        if (chatRoom == null) {
            // 채팅방이 존재하지 않으면 새로 생성
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

            // seller를 user로 설정할 수도 있고, 필요에 따라 다른 유저를 설정할 수 있음
            ChatRoom newChatRoom = ChatRoom.create(user, user, product); // 현재 user를 buyer와 seller로 설정, 필요에 따라 seller를 구분

            // 채팅방 저장
            chatRoomRepository.save(newChatRoom);
            return newChatRoom;
        } else {
            // 채팅방이 조회되었으나, 사용자나 제품 정보가 맞지 않는 경우 예외 처리
            if (!chatRoom.getBuyer().equals(user) || !chatRoom.getProduct().getId().equals(productId)) {
                throw new IllegalArgumentException("채팅방 정보가 일치하지 않습니다.");
            }
        }

        // 조회된 채팅방 반환
        return chatRoom;
    }

}
