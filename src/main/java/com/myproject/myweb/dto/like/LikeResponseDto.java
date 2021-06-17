package com.myproject.myweb.dto.like;

import com.myproject.myweb.domain.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResponseDto {
    private Long id;
    private Long postId;
    private String postTitle;
    private Long userId;
    private String userName;

    public LikeResponseDto(Like entity){
        this.id = entity.getId();
        this.postId = entity.getPost().getId();
        this.postTitle = entity.getPost().getTitle();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getName();
    }
}
