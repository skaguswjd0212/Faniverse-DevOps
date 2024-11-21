package fantastic.faniverse.community.repository;

import fantastic.faniverse.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoardId(Integer boardId, Pageable pageable);
    Page<Post> findByBoardIdAndTitleContaining(Long boardId, String title, Pageable pageable);
}
