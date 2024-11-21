package fantastic.faniverse.community.service;

import fantastic.faniverse.community.controller.dto.PostResponse;
import fantastic.faniverse.community.domain.Post;

import java.util.List;

public interface PostService {
    List<Post> searchPostsByTitle(Long boardId, String title, Integer page, Integer pageSize);

    Post getPost(Long postId);

    List<Post> getBoardPostList(Integer boardId, Integer page, Integer pageSize);

    void createPost(Post req, Long userId);

    void updatePost(Post form, Long postId, Long userId);

    void deletePost(Long postId, Long userId);

    void likesPost(Long postId, Long userId);
}
