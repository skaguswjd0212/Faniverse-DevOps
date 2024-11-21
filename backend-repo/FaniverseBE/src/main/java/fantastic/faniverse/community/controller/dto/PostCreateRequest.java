package fantastic.faniverse.community.controller.dto;

import fantastic.faniverse.community.domain.Board;
import fantastic.faniverse.community.domain.Post;
import lombok.Getter;

@Getter
public class PostCreateRequest {
    private String title;
    private String content;
    private String imageUrl;
    private Long boardId;

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .imageUrl(this.imageUrl)
                .board(Board.builder()
                        .id(this.boardId)
                        .build())
                .build();
    }
}
