package com.myproject.myweb.dto.user.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class UserPostQueryDto {

    @JsonIgnore
    private Long userId;

    private Long myPostId;
    private String myPostCategory;
    private String myPostTitle;
    private String myPostContent;

    private Boolean myPostIsPublic;
    private Boolean myPostIsComplete;
    private Long myPostTotalLike;

    // likeList 는 나중에 추가해보기

    public UserPostQueryDto(Long userId, Long myPostId, String myPostCategory, String myPostTitle,
                            String myPostContent, Boolean myPostIsPublic, Boolean myPostIsComplete){

        this.userId = userId;
        this.myPostId = myPostId;
        this.myPostCategory = myPostCategory;
        this.myPostTitle = myPostTitle;
        this.myPostContent = myPostContent;
        this.myPostIsPublic = myPostIsPublic;
        this.myPostIsComplete = myPostIsComplete;
    }

    public void addTotalLike(Long totalLike){
        this.myPostTotalLike = totalLike;
    }

}
