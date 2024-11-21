package fantastic.faniverse.chat.infrastructure.repository;

import fantastic.faniverse.chat.domain.ChatRoom;
import fantastic.faniverse.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.user WHERE (cr.user = :user OR cr.buyer = :user) AND cr.isDeleted IS NOT TRUE")
    List<ChatRoom> findWithUserByUserAndIsDeletedNot(@Param("user") User user);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.id = :id AND (cr.user = :user OR cr.buyer = :user) AND cr.isDeleted IS NOT TRUE")
    Optional<ChatRoom> findByUserAndId(User user, Long id);

    Optional<ChatRoom> findById(Long id);
}
