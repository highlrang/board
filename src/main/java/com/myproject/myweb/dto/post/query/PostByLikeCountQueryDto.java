package com.myproject.myweb.dto.post.query;

import lombok.Getter;

@Getter
public class PostByLikeCountQueryDto {
    private Long postId;
    private String categoryName;
    private String postTitle;
    private String writerName;
    private Boolean isComplete;
    private Long postLikeCount;

    public PostByLikeCountQueryDto(Long postId, String categoryName, String postTitle,
                                   String writerName, Boolean isComplete, Long postLikeCount){
        this.postId = postId;
        this.categoryName = categoryName;
        this.postTitle = postTitle;
        this.writerName = writerName;
        this.isComplete = isComplete;
        this.postLikeCount = postLikeCount;
    }
}
