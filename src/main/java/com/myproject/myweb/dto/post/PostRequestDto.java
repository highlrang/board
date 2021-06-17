package com.myproject.myweb.dto.post;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
public class PostRequestDto {
    @NotNull
    private Long writerId;
    @NotNull
    private Long categoryId;
    @NotNull
    private String title;
    private String content;
    private Boolean isPublic;

    // <save>
    @Builder
    public PostRequestDto(Long categoryId, Long writerId, String title, String content, Boolean isPublic){
        this.categoryId = categoryId;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }


    public Post toEntity(){ // Dto로 Post 빌더 호출 >> category와 writer는 id로 entity 찾아서 이후에 add
        Post post = Post.builder()
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .build();
        return post;
    }

}