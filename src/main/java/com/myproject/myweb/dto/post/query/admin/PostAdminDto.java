package com.myproject.myweb.dto.post.query.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class PostAdminDto {

    private Long id;
    private String name;
    private String category;

    private List<Long> postIds = new ArrayList<>(); // 담당 게시글

    @Builder
    public PostAdminDto(Long id, String name, String category){
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public void addPostIds(List<Long> postIds){
        this.postIds = postIds;
    }
}