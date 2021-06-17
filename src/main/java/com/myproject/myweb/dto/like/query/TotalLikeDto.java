package com.myproject.myweb.dto.like.query;

import com.myproject.myweb.domain.user.User;
import lombok.Getter;

@Getter
public class TotalLikeDto {
    private Long id;
    private Long totalLike;

    public TotalLikeDto(Long id, Long totalLike) {
        this.id = id;
        this.totalLike = totalLike;

    }

}

