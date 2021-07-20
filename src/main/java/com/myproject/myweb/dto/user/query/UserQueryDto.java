package com.myproject.myweb.dto.user.query;

import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class UserQueryDto {

    private Long userId;
    private String userEmail;
    private String userName;
    private Role userRole; // title

    private List<UserLikeQueryDto> userLikes;
    private List<UserPostQueryDto> userPosts;

    public UserQueryDto(Long userId, String userEmail, String userName, Role userRole){
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userRole = userRole;
    }

    public void addUserLikes(List<UserLikeQueryDto> userLikes){
        this.userLikes = userLikes;
    }

    public void addUserPosts(List<UserPostQueryDto> userPosts){
        this.userPosts = userPosts;
    }
}
