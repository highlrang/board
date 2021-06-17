package com.myproject.myweb.dto.user.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class UserLikeQueryDto {
    // 좋아요한 게시글 정보
    @JsonIgnore
    private Long userId;

    private Long likedPostId;
    private String likedPostWriter; // name
    private String likedPostCategory;

    private String likedPostTitle;
    private Long likedPostTotalLike;
    private Boolean likedPostComplete;

    public UserLikeQueryDto(Long userId, Long likedPostId,
                            String likedPostWriter, String likedPostCategory,
                            String likedPostTitle, Boolean likedPostComplete){

        this.userId = userId;
        this.likedPostId = likedPostId;
        this.likedPostWriter = likedPostWriter;
        this.likedPostCategory = likedPostCategory;
        this.likedPostTitle = likedPostTitle;
        this.likedPostComplete = likedPostComplete;

    }

    public void addTotalLike(Long totalLike){
        this.likedPostTotalLike = totalLike;
    }
}
