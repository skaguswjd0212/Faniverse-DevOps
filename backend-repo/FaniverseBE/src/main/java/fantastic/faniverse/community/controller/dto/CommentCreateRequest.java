package fantastic.faniverse.community.controller.dto;

import fantastic.faniverse.community.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentCreateRequest {
    private String content;
    public Comment toEntity() {
        return Comment.builder()
                .content(this.content)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
