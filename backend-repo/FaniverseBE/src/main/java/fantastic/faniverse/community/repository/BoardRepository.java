package fantastic.faniverse.community.repository;

import fantastic.faniverse.community.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findBoardByNameContaining(String name, Pageable pageable);
}
