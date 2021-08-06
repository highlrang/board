package com.myproject.myweb.dto.user;

import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;

    @Builder
    public UserRequestDto(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void passwordEncode(String password){
        this.password = password;
    }

    public User toEntity(){ // Dto를 User 객체로 변경
        return User.builder()
                .role(Role.NORMAL_USER) //defualt
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

}
