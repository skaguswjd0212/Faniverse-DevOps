package fantastic.faniverse.community.repository;

import fantastic.faniverse.community.domain.Board;
import fantastic.faniverse.community.domain.FavoriteBoard;
import fantastic.faniverse.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteBoardRepository extends JpaRepository<FavoriteBoard, Long> {
    Optional<FavoriteBoard> findByBoardAndUser(Board board, User user);
}
