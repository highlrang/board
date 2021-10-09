package com.myproject.myweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myproject.myweb.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert // set하지 않은 것을 null로 안 넣음 == 후에 default값 적용 >> 이것은 @PrePersist 로도 할 수 있음
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer_id")// name 은 fk 이름, reference는 참조할 컬럼(기본 pk)
    private User writer;

    //OneToMany 관계에서는 학생 클래스에 학교의 id값만 집어넣어서 간접적으로 참조만 했지만,
    //ManyToOne 관계를 적용하면 각자의 학교 엔티티를 직접 소유하게 할 수 있다.
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY) // proxy
    @JoinColumn(name="category_id") // name 꼭 지정해줄 것 안 했더니 기본이 id로 들어감 ㅜㅜ 왜지? 당연히 category일 줄 알았는데
    private Category category;

    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isPublic;

    @Column(columnDefinition = "boolean default false")
    private Boolean isComplete;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<>();

    @Builder
    public Post(String title, String content, Boolean isPublic){
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }

    public static Post createPost(String title, String content, Boolean isPublic, Category category, User writer){
        Post post = Post.builder().title(title).content(content).isPublic(isPublic).build();
        post.addCategory(category);
        post.addWriter(writer);
        return post;
    }

    public void addCategory(Category category){
        this.category = category;
    }

    public void addWriter(User writer){
        this.writer = writer;
    }

    public void likeDelete(Like l){
        this.likeList.remove(l);
    }

    public void update(String title, String content, Boolean isPublic){
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }

    public void completeUpdate(Boolean isComplete){
        this.isComplete = isComplete;
    }
}
