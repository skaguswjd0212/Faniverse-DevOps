package fantastic.faniverse.community.controller;

import fantastic.faniverse.community.controller.dto.CommentCreateRequest;
import fantastic.faniverse.community.controller.dto.CommentResponse;
import fantastic.faniverse.community.controller.dto.CommentUpdateForm;
import fantastic.faniverse.community.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @PostMapping("/create/{postId}")
    public ResponseEntity<Object> creatComment(
            @PathVariable("postId") Long postId,
            @RequestBody @Validated CommentCreateRequest request,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            logger.error("User ID is null. Make sure the user is logged in.");
            return ResponseEntity.status(401).body("User not logged in");
        }

        commentService.createComment(request.toEntity(), postId, userId);
        logger.info("User ID: " + userId + " created a comment on Post ID: " + postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{postId}")
    public ResponseEntity<Object> getCommentList(
            @PathVariable("postId") Long postId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<CommentResponse> commentList = commentService
                .getCommentList(postId, page, pageSize).stream()
                .map(CommentResponse::convertCommentResponse).collect(Collectors.toList());
        return ResponseEntity.ok(commentList);
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity<Object> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody @Validated CommentUpdateForm updateForm,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            logger.error("User ID is null. Make sure the user is logged in.");
            return ResponseEntity.status(401).body("User not logged in");
        }

        commentService.updateComment(updateForm.toEntity(), commentId, userId);
        logger.info("User ID: " + userId + " updated Comment ID: " + commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable("commentId") Long commentId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            logger.error("User ID is null. Make sure the user is logged in.");
            return ResponseEntity.status(401).body("User not logged in");
        }

        commentService.delete(commentId, userId);
        logger.info("User ID: " + userId + " deleted Comment ID: " + commentId);
        return ResponseEntity.ok().build();
    }
}
