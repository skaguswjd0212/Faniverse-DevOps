package fantastic.faniverse.community.controller.dto;

import fantastic.faniverse.community.domain.Comment;
import lombok.Getter;

@Getter
public class CommentUpdateForm {
    private String content;
    public Comment toEntity() {
        return Comment.builder()
                .content(this.content)
                .build();
    }
}
