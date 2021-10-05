package com.myproject.myweb.domain;

import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.like.query.TotalLikeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
@Table(name="Likes")
@SqlResultSetMapping(
        name = "likes_totalLike_dto",
        classes = @ConstructorResult(
                targetClass = TotalLikeDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "totalLike", type = Long.class)
                }
        )
)
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public Like(Post post, User user){
        this.post = post;
        this.user = user;
    }


    /* 연관관계 편의 메서드
    public void addLikeAndPost(Post post){
        this.post = post;
        post.getLikeList().add(this);

        // cascade 등으로 영속화 되어야지 변경감지로 메서드 실행 가능함
    }*/

    // 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없습니다. 단지 엔티티를 영속화 할 때 연관된 엔티티도 영속성 컨텍스트에 포함되는 편리함
    // 참조하는 엔티티가 하나일 때, orphanRemoval = true로 고아 객체 삭제 시킬 수 있음
    // 두 개 이상이면 삭제 시 연관관계 편의 메서드로. (ex. 회원 탈퇴 시 자식 객체 삭제) - 영속성 일치

}
