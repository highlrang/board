package com.myproject.myweb.dto.post;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.dto.like.LikeResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private Long writerId;
    private String writerName;
    private String title;
    private String content;
    private Boolean isPublic;
    private Boolean isComplete;
    private Long totalLike;
    private List<LikeResponseDto> likes;

    public PostDetailResponseDto(Post entity){
        this.id = entity.getId();
        this.categoryId = entity.getCategory().getId();
        this.categoryName = entity.getCategory().getName();
        this.writerId = entity.getWriter().getId();
        this.writerName = entity.getWriter().getName();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.isPublic = entity.getIsPublic();
        this.isComplete = entity.getIsComplete();
    }

    public void addTotalLike(Long totalLike){
        this.totalLike = totalLike;
    }

    public void addLikeList(List<LikeResponseDto> likeList){
        this.likes = likeList;
    }

}
