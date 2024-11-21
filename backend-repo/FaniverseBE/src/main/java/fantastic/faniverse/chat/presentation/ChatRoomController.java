package fantastic.faniverse.chat.presentation;

import fantastic.faniverse.chat.application.ChatRoomService;
import fantastic.faniverse.chat.presentation.response.CharRoomResponse;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<CharRoomResponse>> chat(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(null);  // 로그인되지 않은 경우
        }

        User currentUser = userService.findUserById(userId);
        return ResponseEntity.ok(chatRoomService.findChatRoomByUser(currentUser)
                .stream()
                .map(CharRoomResponse::of)
                .toList());
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long roomId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(null);  // 로그인되지 않은 경우
        }

        User currentUser = userService.findUserById(userId);
        chatRoomService.deleteChatRoom(roomId, currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block/{roomId}")
    public ResponseEntity<Void> blockChatRoom(@PathVariable Long roomId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(null);  // 로그인되지 않은 경우
        }

        User currentUser = userService.findUserById(userId);
        chatRoomService.blockChatRoom(roomId, currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create/{sellerId}/{productId}")
    public ResponseEntity<Void> createChatRoom(@PathVariable Long sellerId, @PathVariable Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(null);  // 로그인되지 않은 경우
        }

        User currentUser = userService.findUserById(userId);
        chatRoomService.createChatRoom(currentUser, sellerId, productId);
        return ResponseEntity.ok().build();
    }
}
