package fantastic.faniverse.community.controller;

import fantastic.faniverse.community.controller.dto.PostCreateRequest;
import fantastic.faniverse.community.controller.dto.PostResponse;
import fantastic.faniverse.community.controller.dto.PostUpdateForm;
import fantastic.faniverse.community.domain.Post;
import fantastic.faniverse.community.service.PostService;
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
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/search")
    public ResponseEntity<Object> searchPostsByTitle(
            @RequestParam(value = "boardId") Long boardId,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<PostResponse> list = postService
                .searchPostsByTitle(boardId, title, page, pageSize).stream()
                .map(PostResponse::convertPostResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Object> getPost(
            @PathVariable("postId") Long postId
    ) {
        Post post = postService.getPost(postId);
        PostResponse postResponse = PostResponse.convertPostResponse(post);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getBoardPostList(
            @RequestParam(value = "boardId") Integer boardId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<PostResponse> list = postService
                .getBoardPostList(boardId, page, pageSize).stream()
                .map(PostResponse::convertPostResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(
            @RequestBody @Validated PostCreateRequest request,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            logger.error("User not logged in or session expired");
            return ResponseEntity.status(401).body("User not logged in or session expired");
        }
        postService.createPost(request.toEntity(), userId);
        logger.info("User ID: " + userId + " created a post.");
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<Object> updatePost(
            @RequestBody @Validated PostUpdateForm updateForm,
            @PathVariable("postId") Long postId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            logger.error("User not logged in or session expired");
            return ResponseEntity.status(401).body("User not logged in or session expired");
        }
        postService.updatePost(updateForm.toEntity(), postId, userId);
        logger.info("User ID: " + userId + " updated Post ID: " + postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Object> deletePost(
            @PathVariable("postId") Long postId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            logger.error("User not logged in or session expired");
            return ResponseEntity.status(401).body("User not logged in or session expired");
        }
        postService.deletePost(postId, userId);
        logger.info("User ID: " + userId + " deleted Post ID: " + postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/likes/{postId}")
    public ResponseEntity<Object> likesPost(
            @PathVariable("postId") Long postId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            logger.error("User not logged in or session expired");
            return ResponseEntity.status(401).body("User not logged in or session expired");
        }
        postService.likesPost(postId, userId);
        logger.info("User ID: " + userId + " liked Post ID: " + postId);
        return ResponseEntity.ok().build();
    }
}
