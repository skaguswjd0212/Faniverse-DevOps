package fantastic.faniverse.keyword.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordDto {
    private Long id;
    private String word;
    private Long userId;

    // 기본 생성자
    public KeywordDto() {}

    // 모든 필드를 매개변수로 받는 생성자
    public KeywordDto(Long id, String word, Long userId) {
        this.id = id;
        this.word = word;
        this.userId = userId;
    }
}
