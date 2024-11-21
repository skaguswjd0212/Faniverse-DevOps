package fantastic.faniverse.community.controller.dto;

import fantastic.faniverse.community.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer likeCount;
    private String imageUrl;
    private Long userId;
    private Long boardId;

    public PostResponse (Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.likeCount = post.getLikeCount();
        this.imageUrl = post.getImageUrl();
        this.userId = post.getUser().getId();
        this.boardId = post.getBoard().getId();
    }
    public static PostResponse convertPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .likeCount(post.getLikeCount())
                .imageUrl(post.getImageUrl())
                .userId(post.getUser().getId())
                .boardId(post.getBoard().getId())
                .build();
    }
}
