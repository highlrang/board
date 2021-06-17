package com.myproject.myweb.dto.user.query;

import com.myproject.myweb.domain.user.Role;
import lombok.Getter;

@Getter
public class WriterByLikeCountQueryDto {
    private Long userId;
    private String userRole;
    private String userEmail;
    private String userName;

    private Long writerLikeCount;

    public WriterByLikeCountQueryDto(Long userId, Role userRole,
                                     String userEmail, String userName,
                                     Long writerLikeCount) {
        this.userId = userId;
        this.userRole = userRole.getTitle();
        this.userEmail = userEmail;
        this.userName = userName;
        this.writerLikeCount = writerLikeCount;
    }
}
