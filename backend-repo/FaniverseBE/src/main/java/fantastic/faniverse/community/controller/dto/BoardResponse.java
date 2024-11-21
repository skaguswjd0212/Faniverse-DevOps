package fantastic.faniverse.community.controller.dto;

import fantastic.faniverse.community.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardResponse {
    private String name;
    public static BoardResponse convertBoardResponse(Board board) {
        return BoardResponse.builder()
                .name(board.getName())
                .build();
    }
}
