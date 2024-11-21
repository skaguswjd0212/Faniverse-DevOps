package fantastic.faniverse.community.service;

import fantastic.faniverse.community.domain.Comment;
import fantastic.faniverse.community.domain.Post;
import fantastic.faniverse.community.repository.CommentRepository;
import fantastic.faniverse.community.repository.PostRepository;

import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void createComment(Comment req, Long postId, Long userId) {
        Post post = getPost(postId);
        User user = getUser(userId);
        Comment commentBuild = Comment.builder()
                .content(req.getContent())
                .createdAt(req.getCreatedAt())
                .post(post)
                .user(user)
                .build();
        commentRepository.save(commentBuild);
    }

    @Override
    public List<Comment> getCommentList(Long postId, Integer page, Integer pageSize) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, pageSize, sortBy);
        return commentRepository.findByPostId(postId, pageable).getContent();
    }

    @Override
    @Transactional
    public void updateComment(Comment form, Long commentId, Long userId) {
        User user = getUser(userId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 못 찾음"));
        if (comment.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("권한 없음");
        }
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setContent(form.getContent());
    }

    @Override
    public void delete(Long commentId, Long userId) {
        User user = getUser(userId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("글 없음"));
        if (comment.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("권한 없음");
        }
        commentRepository.delete(comment);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("글 없음"));
    }
}
