package com.myproject.myweb.dto;

import com.myproject.myweb.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String name;

    public CategoryResponseDto(Category entity){
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
