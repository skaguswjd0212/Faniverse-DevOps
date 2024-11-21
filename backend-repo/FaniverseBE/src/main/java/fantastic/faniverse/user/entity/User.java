package fantastic.faniverse.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> recentSearches = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public void addRecentSearch(String searchWord) {
        if (recentSearches.size() >= 5) {
            recentSearches.remove(0); // 가장 오래된 검색어 제거
        }
        recentSearches.add(searchWord); // 새로운 검색어 추가
    }
}
