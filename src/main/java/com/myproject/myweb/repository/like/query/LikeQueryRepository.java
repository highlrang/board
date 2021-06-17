package com.myproject.myweb.repository.like.query;


import com.myproject.myweb.dto.like.query.TotalLikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LikeQueryRepository {

    private final EntityManager em;

    /* v4 ~ v5 dto 이용

    // postId로 like list	(user 정보 잘 드러나게)
    // v4 dto
    public List<LikeUserQuery> findAllLikesByPost(Long postId){ // Dto 따로 만들기? (entity password에 @JsonIgnore)

        // user의 모든 것을 뽑을거면 toMany관계에 @JsonIgnore
        // 안되면 userQueryDto로 바로 받기

        List<LikeUserQuery> users = em.createQuery("select new com.myproject.myweb.dto.like.query.LikeUserQuery(p.title, u.id, u.email, u.name, u.role)" +
                " from Like l" +
                " join l.post p" +
                " join l.user u" + // 1 + n 쿼리 문제 확인하기
                " where l.post.id = :postId", LikeUserQuery.class)
                .setParameter("postId", postId)
                .getResultList();

        return users;
    }

    // userId로 like list	(post 정보 ..)
    // v3
    public List<Like> findAllLikesByUser(Long userId){
        // post entity의 likeList @JsonIgnore함
        List<Like> likes = em.createQuery("select l" +
                " from Like l" +
                " join fetch l.post p" +
                " join fetch p.writer w" +
                " join fetch p.category c" +
                " where l.user.id = :userId", Like.class)
                .setParameter("userId", userId)
                .getResultList();

        return likes;
    }

    // fetch는 lazy 지연 로딩 해결하기 위한 방법
    // like > post > writer을 모두 꺼내줌
    // 하지만 컬렉션의 경우 1 + n + n의 문제(fetch join 사용할 경우 distinct 필요)
    // fetch join 말고 다른 방법으로는 v3.1 or v5(dto)

    */

    public Map<Long, Long> findAllLikesByPostsWriters(List<Long> userIds){
        Query entity = em.createNativeQuery(
                "select p.writer_id as id, count(*) as totalLike" +
                        " from Likes l" +
                        " join Post p on l.post_id = p.id" +
                        " where p.writer_id in :userIds" +
                        " group by p.writer_id"
                , "likes_totalLike_dto")
                .setParameter("userIds", userIds);

        List<TotalLikeDto> result = entity.getResultList();

        Map<Long, Long> likes = result.stream()
                .collect(Collectors.toMap(
                        e -> e.getId(),
                        e -> e.getTotalLike()
                ));

        return likes;
    }

    public Map<Long, Long> findAllLikesByPostsIds(List<Long> postIds){
        Query entity = em.createNativeQuery(
                "select p.id as id, count(*) as totalLike" +
                        " from Likes l" +
                        " join Post p on l.post_id = p.id" +
                        " where p.id in :postIds" +
                        " group by l.post_id"
                , "likes_totalLike_dto")
                .setParameter("postIds", postIds);

        List<TotalLikeDto> result = entity.getResultList();

        Map<Long, Long> likes = result.stream()
                .collect(Collectors.toMap(e -> e.getId(),
                                          e -> e.getTotalLike()));

        return likes;

    }


}
