package fantastic.faniverse.keyword.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordProductDto {
    private Long productId;
    private String word;
    private String title;
    private String content;
    private String category;
    private String imageUrl;
    private Double price;
}
