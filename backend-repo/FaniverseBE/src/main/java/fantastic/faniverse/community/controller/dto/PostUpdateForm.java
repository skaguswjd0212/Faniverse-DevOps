package fantastic.faniverse.community.controller.dto;

import fantastic.faniverse.community.domain.Post;
import lombok.Getter;

@Getter
public class PostUpdateForm {
    private String title;
    private String content;
    private String imageUrl;

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .imageUrl(this.imageUrl)
                .build();
    }
}
