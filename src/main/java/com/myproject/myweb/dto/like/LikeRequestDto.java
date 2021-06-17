package com.myproject.myweb.dto.like;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequestDto {
    private Long postId;
    private Long userId;

    @Builder
    public LikeRequestDto(Long postId, Long userId){
        this.postId = postId;
        this.userId = userId;
    }

    /*
    public Like toEntity(){
        return Like.builder()
                .post(post)
                .user(user)
                .build();
    }
     */

}
