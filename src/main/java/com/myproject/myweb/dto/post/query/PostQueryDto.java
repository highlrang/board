package com.myproject.myweb.dto.post.query;


import com.myproject.myweb.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor // 없어도 될 듯
public class PostQueryDto {
    private Long postId;
    private String cateName;
    private Long userId;
    private String userName;
    private String postTitle;
    private String postContent;
    private Boolean isPublic;
    private Boolean isComplete;
    private Long totalLike;
    private List<PostLikeQueryDto> likeList;

    public PostQueryDto(Long postId, String cateName, Long userId, String userName,
                        String postTitle, String postContent, Boolean isPublic, Boolean isComplete){

        this.postId = postId;
        this.cateName = cateName;
        this.userId = userId;
        this.userName = userName;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.isPublic = isPublic;
        this.isComplete = isComplete;

    }

    public void addTotalLike(Long totalLike){
        this.totalLike = totalLike;
    }

    public void addLikeList(List<PostLikeQueryDto> likes){
        this.likeList = likes;
    }

}
