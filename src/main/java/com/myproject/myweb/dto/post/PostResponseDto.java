package com.myproject.myweb.dto.post;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private Long writerId;
    private String writerName;
    private String title;
    private Boolean isPublic;
    private Boolean isComplete;
    private Long totalLike;

    public PostResponseDto(Post entity){
        this.id = entity.getId();
        this.categoryId = entity.getCategory().getId();
        this.categoryName = entity.getCategory().getName();
        this.writerId = entity.getWriter().getId();
        this.writerName = entity.getWriter().getName();
        this.title = entity.getTitle();
        this.isPublic = entity.getIsPublic();
        this.isComplete = entity.getIsComplete();
    }

    public void addTotalLike(Long totalLike){
        this.totalLike = totalLike;
    }

}
