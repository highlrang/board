package com.myproject.myweb.dto.post.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

// 사용 안 함
@Getter
public class PostLikeQueryDto {

    @JsonIgnore
    private Long postId;

    private Long likeUserId;
    private String likeUserEmail;
    private String likeUserName;

    public PostLikeQueryDto(Long postId, Long likeUserId, String likeUserEmail, String likeUserName){
        this.postId = postId;
        this.likeUserId = likeUserId;
        this.likeUserEmail = likeUserEmail;
        this.likeUserName = likeUserName;
    }



}
