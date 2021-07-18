package com.myproject.myweb.dto.post.query.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BestPostAdminDto {

    private Long id;
    private String name;
    private String category;
    private Integer resolutionDegree;

    @Builder
    public BestPostAdminDto(Long id, String name, String category, Integer resolutionDegree){
        this.id = id;
        this.name = name;
        this.category = category;
        this.resolutionDegree = resolutionDegree;
    }
}