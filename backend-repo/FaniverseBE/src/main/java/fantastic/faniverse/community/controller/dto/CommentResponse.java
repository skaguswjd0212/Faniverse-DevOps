package fantastic.faniverse.community.controller.dto;

import fantastic.faniverse.community.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {
    private Long postId;
    private LocalDateTime createdAt;
    private String content;
    public static CommentResponse convertCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
