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
    private String category;
    private String title;
    private String writer;
    private Boolean isComplete;
    private Long likeCount;

    public PostByLikeCountQueryDto(Long postId, String category, String title,
                                   String writer, Boolean isComplete, Long likeCount){
        this.postId = postId;
        this.category = category;
        this.title = title;
        this.writer = writer;
        this.isComplete = isComplete;
        this.likeCount = likeCount;
    }
}
