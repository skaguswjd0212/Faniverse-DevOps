package fantastic.faniverse.community.service;

import fantastic.faniverse.community.domain.Board;

import java.util.List;

public interface BoardService {
    void createBoard(Board board);

    List<Board> searchBoardsByName(String name, Integer page, Integer pageSize);

    List<Board> getBoardList(Integer page, Integer pageSize);

    void updateBoard(Board entity, Long boardId);

    void deleteBoard(Long boardId);

    void likesBoard(Long boardId, Long userId);
}
