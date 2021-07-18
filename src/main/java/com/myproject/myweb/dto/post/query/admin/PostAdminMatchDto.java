package com.myproject.myweb.dto.post.query.admin; // admin or external

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class PostAdminMatchDto {

    private String category;
    private Long adminId;
    private List<Long> postIds = new ArrayList<>();

    /*
    // member
    private String adminName;
    private Integer adminResolutionDegree;


    // post
    private String postTitle;
    private String postWriter;
    private Boolean postComplete;
    private Long postLike;
    */

    @Builder
    public PostAdminMatchDto(String category, Long adminId){
        this.category = category;
        this.adminId = adminId;
    }

    public void addPostIds(List<Long> postIds){
        this.postIds = postIds;
    }


}
