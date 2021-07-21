package com.myproject.myweb.dto.post.query;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
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
