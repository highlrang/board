package com.myproject.myweb.dto;

import com.myproject.myweb.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 기본 생성자
public class CategoryRequestDto {
    private String name;

    @Builder
    public CategoryRequestDto(String name){
        this.name = name;
    }

    public Category toEntity(){ // requestDto >> entity
        return Category.builder()
                .name(name)
                .build();
    }
}
