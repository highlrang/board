package com.myproject.myweb.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myproject.myweb.domain.BaseTimeEntity;
import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert // 필드 입력 안 할 시 null 대신 default 값으로
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<>();

    @Builder
    public User(Role role, String name, String email, String password){
        this.role = role; // dto에서 default role 지정
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void postDelete(Post p){
        this.postList.remove(p);
    }

    public void likeDelete(Like l){
        this.likeList.remove(l);
    }

    public void nameUpdate(String name){
        this.name = name;
    }

    public void passwordUpdate(String password){
        this.password = password;
    }

    public void roleUpdate(Role role){ // email > id > update
        this.role = role;
    }

    // public String getRoleKey(){ return this.role.getKey(); }
}
