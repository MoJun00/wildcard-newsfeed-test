package com.sparta.wildcard_newsfeed.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class PostPageRequestDto {
    @Schema(description = "선택 페이지", example = "1")
    @NotNull(message = "선택 페이지 필수 입력 값입니다.")
    @Positive(message = "0이 아닌 양수만 가능합니다.")
    private int page; // 현재 페이지 0부터 시작

    @Schema(description = "게시글 수", example = "10")
    @NotNull(message = "게시글 수 필수 입력 값입니다.")
    private int size; // 한 페이지에 보이는 글 개수

    @Schema(description = "정렬 기준", example = "CREATE")
    @NotNull(message = "정렬 기준 필수 입력 값입니다.")
    private String sortBy; // 생성일자 최신순 or 좋아요 많은 순

    @Schema(description = "검색 기간 시작일", example = "2024-05-01")
    private String firstDate; // 생성일자 최신순 or 좋아요 많은 순

    @Schema(description = "검색 기간 마지막일", example = "2024-05-27")
    private String lastDate; // 생성일자 최신순 or 좋아요 많은 순

    /*
    - **페이지네이션**
        - 10개씩 페이지네이션하여, 각 페이지 당 뉴스피드 데이터가 10개씩 나오게 합니다.
    - **정렬 기능**
        - 생성일자 기준 최신순
        - 좋아요 많은 순
    - **기간별 검색 기능**
        - 예) 2024.05.01 ~ 2024.05.27 동안 작성된 뉴스피드 게시물 검색
     */
}