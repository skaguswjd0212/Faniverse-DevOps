package fantastic.faniverse.community.service;

import fantastic.faniverse.community.domain.Board;
import fantastic.faniverse.community.domain.Likes;
import fantastic.faniverse.community.domain.Post;
import fantastic.faniverse.community.repository.BoardRepository;
import fantastic.faniverse.community.repository.LikesRepository;
import fantastic.faniverse.community.repository.PostRepository;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;

    @Override
    public List<Post> searchPostsByTitle(Long boardId, String title, Integer page, Integer pageSize) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, pageSize, sortBy);
        return postRepository
                .findByBoardIdAndTitleContaining(boardId, title, pageable)
                .getContent();
    }


    @Override
    @Transactional(readOnly = true)
    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> getBoardPostList(Integer boardId, Integer page, Integer pageSize) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, pageSize, sortBy);
        return postRepository.findByBoardId(boardId, pageable).getContent();
    }


    @Override
    public void createPost(Post req, Long userId) {
        Board board = boardRepository.findById(req.getBoard().getId())
                .orElseThrow(() -> new IllegalArgumentException("커뮤니티 게시판 없음"));
        User user = getUser(userId);
        Post postBuild = Post.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .createdAt(now())
                .updatedAt(now())
                .imageUrl(req.getImageUrl())
                .likeCount(0)
                .user(user)
                .board(board)
                .build();
        postRepository.save(postBuild);
    }

    @Override
    @Transactional
    public void updatePost(Post form, Long postId, Long userId) {
        User user = getUser(userId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("글 못 찾음"));
        if (post.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("권한 없음");
        }
        post.setUpdatedAt(now());
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setImageUrl(form.getImageUrl());
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        User user = getUser(userId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("글 못 찾음"));
        if (post.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("권한 없음");
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void likesPost(Long postId, Long userId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (postOpt.isPresent() && userOpt.isPresent()) {
            Post post = postOpt.get();
            User user = userOpt.get();

            Optional<Likes> likesOpt = likesRepository.findByUserAndPost(user, post);

            if (likesOpt.isEmpty()) {
                Likes likesBuild = Likes.builder()
                        .user(user)
                        .post(post)
                        .build();
                likesRepository.save(likesBuild);
            } else {
                likesRepository.delete(likesOpt.get());
            }
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 못 찾음"));
    }
}
