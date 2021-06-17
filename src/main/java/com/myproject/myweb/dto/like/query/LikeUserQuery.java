package com.myproject.myweb.dto.like.query;

import com.myproject.myweb.domain.user.Role;
import lombok.Getter;

// 사용 안 함
@Getter
public class LikeUserQuery { // 게시글에 좋아요한 사용자 정보
    private String postTitle;

    private Long userId;
    private String userEmail;
    private String userName;
    private Role userRole;

    public LikeUserQuery(String postTitle, Long userId, String userEmail,
                         String userName, Role userRole){
        this.postTitle = postTitle;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userRole = userRole;
    }

}
