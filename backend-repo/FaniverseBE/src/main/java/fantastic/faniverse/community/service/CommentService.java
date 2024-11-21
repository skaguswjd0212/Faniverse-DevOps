package fantastic.faniverse.community.service;

import fantastic.faniverse.community.domain.Comment;

import java.util.List;

public interface CommentService {
    void createComment(Comment entity, Long postId, Long userId);

    List<Comment> getCommentList(Long postId, Integer page, Integer pageSize);

    void updateComment(Comment entity, Long commentId, Long userId);

    void delete(Long commentId, Long userId);
}
