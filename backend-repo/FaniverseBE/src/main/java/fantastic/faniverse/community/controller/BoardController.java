package fantastic.faniverse.community.controller;

import fantastic.faniverse.community.controller.dto.BoardCreateRequest;
import fantastic.faniverse.community.controller.dto.BoardUpdateForm;
import fantastic.faniverse.community.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @PostMapping("/create")
    public ResponseEntity<Object> createBoard(@RequestBody @Validated BoardCreateRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            logger.error("User ID is null. Make sure the user is logged in.");
            return ResponseEntity.status(401).body("User not logged in");
        }

        boardService.createBoard(request.toEntity());  // 원래대로 toEntity() 호출
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchBoardsByName(@RequestParam(value = "name") String name,
                                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(boardService.searchBoardsByName(name, page, pageSize));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getBoardList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(boardService.getBoardList(page, pageSize));
    }

    @PatchMapping("/update/{boardId}")
    public ResponseEntity<Object> updateBoard(@PathVariable("boardId") Long boardId,
                                              @RequestBody @Validated BoardUpdateForm updateForm, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            logger.error("User ID is null. Make sure the user is logged in.");
            return ResponseEntity.status(401).body("User not logged in");
        }

        boardService.updateBoard(updateForm.toEntity(), boardId);  // userId 파라미터 제거
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable("boardId") Long boardId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            logger.error("User ID is null. Make sure the user is logged in.");
            return ResponseEntity.status(401).body("User not logged in");
        }

        boardService.deleteBoard(boardId);  // userId 파라미터 제거
        return ResponseEntity.ok().build();
    }

    @PostMapping("/likes/{boardId}")
    public ResponseEntity<Object> likesBoard(@PathVariable("boardId") Long boardId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            logger.error("User ID is null. Make sure the user is logged in.");
            return ResponseEntity.status(401).body("User not logged in");
        }

        logger.info("User ID: " + userId + " is liking Board ID: " + boardId);
        boardService.likesBoard(boardId, userId);
        return ResponseEntity.ok().build();
    }
}

