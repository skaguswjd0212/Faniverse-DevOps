package fantastic.faniverse.product.ProductCategory.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCategory {

    // 최상위 카테고리
    MUSIC("음악"),
    SPORTS("스포츠"),
    ANI("애니"),
    GAME("게임"),

    // 음악 하위 카테고리
    GIRLGROUP("걸그룹"),
    BOYGROUP("보이그룹"),
    SOLO("솔로"),
    TROT("트로트"),
    ETC("기타"),

    // 스포츠 하위 카테고리
    FOOTBALL("축구"),
    BASKETBALL("농구"),
    VOLLEYBALL("배구"),
    BASEBALL("야구");

    private final String title;

    // 자식 카테고리를 미리 하드코딩한 맵
    private static final Map<String, List<String>> CATEGORY_MAP = Map.of(
            "음악", List.of("걸그룹", "보이그룹", "솔로", "트로트", "기타"),
            "스포츠", List.of("축구", "농구", "배구", "야구")
    );

    // 특정 상위 카테고리의 자식 카테고리 반환
    public static List<String> getChildCategories(String parentTitle) {
        // 자식 카테고리가 없으면 해당 상위 카테고리 자체를 리스트로 반환
        return CATEGORY_MAP.getOrDefault(parentTitle, List.of(parentTitle));
    }

    // 모든 카테고리 반환 (최상위와 하위 포함)
    public static List<ProductCategory> getAllCategories() {
        return List.of(values());
    }

    // 최상위 카테고리만 반환
    public static List<String> getRootCategories() {
        return List.of("음악", "스포츠", "애니", "게임");
    }
}