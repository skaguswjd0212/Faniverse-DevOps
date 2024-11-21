package fantastic.faniverse.community.repository;

import fantastic.faniverse.community.domain.Likes;
import fantastic.faniverse.community.domain.Post;
import fantastic.faniverse.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndPost(User user, Post post);
}
