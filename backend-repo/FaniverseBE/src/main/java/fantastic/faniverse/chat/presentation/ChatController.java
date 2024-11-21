package fantastic.faniverse.chat.presentation;

import fantastic.faniverse.chat.application.ChatService;
import fantastic.faniverse.chat.presentation.request.MessageRequest;
import fantastic.faniverse.chat.presentation.response.MessageResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/send/{roomId}")
    public ResponseEntity<Void> send(
            @RequestBody MessageRequest messageRequest,
            @PathVariable Long roomId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(null);  // 로그인되지 않은 경우
        }

        chatService.createMessage(userId, roomId, messageRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{roomId}")
    public ResponseEntity<List<MessageResponse>> list(
            @PathVariable Long roomId,
            @RequestParam Long lastMessageId
    ) {
        return ResponseEntity.ok(
                chatService.findLastMessages(roomId, lastMessageId).stream()
                        .map(MessageResponse::of)
                        .toList()
        );
    }
}
