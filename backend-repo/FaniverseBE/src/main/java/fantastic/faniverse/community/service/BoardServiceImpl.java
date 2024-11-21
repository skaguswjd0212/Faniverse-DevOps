package fantastic.faniverse.community.service;

import fantastic.faniverse.community.domain.Board;
import fantastic.faniverse.community.domain.FavoriteBoard;
import fantastic.faniverse.community.repository.BoardRepository;
import fantastic.faniverse.community.repository.FavoriteBoardRepository;
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

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FavoriteBoardRepository favoriteBoardRepository;

    @Override
    public void createBoard(Board req) {
        Board boardBuild = Board.builder()
                .name(req.getName())
                .build();
        boardRepository.save(boardBuild);
    }

    @Override
    public List<Board> searchBoardsByName(String name, Integer page, Integer pageSize) {
        Sort sortBy = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, pageSize, sortBy);
        return boardRepository.findBoardByNameContaining(name, pageable).getContent();
    }

    @Override
    public List<Board> getBoardList(Integer page, Integer pageSize) {
        Sort sortBy = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, pageSize, sortBy);
        return boardRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public void updateBoard(Board form, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시판 없음"));
        board.setName(form.getName());
    }

    @Override
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시판 못 찾음"));
        boardRepository.delete(board);
    }

    @Override
    public void likesBoard(Long boardId, Long userId) {
        Optional<Board> boardOpt = boardRepository.findById(boardId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (boardOpt.isPresent() && userOpt.isPresent()) {
            Board board = boardOpt.get();
            User user = userOpt.get();

            Optional<FavoriteBoard> favoriteBoardOpt = favoriteBoardRepository.findByBoardAndUser(board, user);

            if (favoriteBoardOpt.isEmpty()) {
                FavoriteBoard favoriteBoardBuild = FavoriteBoard.builder()
                        .board(board)
                        .user(user)
                        .build();
                favoriteBoardRepository.save(favoriteBoardBuild);
            } else {
                favoriteBoardRepository.delete(favoriteBoardOpt.get());
            }
        }
    }
}
