package com.myproject.myweb.repository.post.query;

import com.myproject.myweb.domain.Post;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.dto.post.query.PostLikeQueryDto;
import com.myproject.myweb.dto.post.query.PostQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// join fetch는 쿼리에서 반환 객체가 dto가 아닌 entity일 때만 가능
@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final EntityManager em;

    public List<PostQueryDto> findAllPostsByDto(){
        // 1. 단순 fetch join
        List<PostQueryDto> posts = findAllPosts();
        // 2-1. List<Long> postId 불러모으기
        List<Long> postIds = getPostIds(posts);
        // 2-2. 일대다 관계인 likes 불러오고 >> postId로 불러온 list 정렬
        Map<Long, List<PostLikeQueryDto>> postLikeMap = findPostLikes(postIds);
        // 3. 둘이 합치기
        posts.forEach(p -> p.addLikeList(postLikeMap.get(p.getPostId())));
        return posts;
    }

    private Map<Long, List<PostLikeQueryDto>> findPostLikes(List<Long> postIds) {
        List<PostLikeQueryDto> likes =
                em.createQuery("select new com.myproject.myweb.dto.post.query.PostLikeQueryDto(p.id, u.id, u.email, u.name)" +
                        " from Like l"+
                        " join l.user u" +
                        " join l.post p" +
                        " where l.post.id in :postIds", PostLikeQueryDto.class)
                .setParameter("postIds", postIds)
                .getResultList();

        // postId로 정렬
        Map<Long, List<PostLikeQueryDto>> postLikeMap =
                likes.stream()
                .collect(Collectors.groupingBy(PostLikeQueryDto::getId));
        return postLikeMap;
    }

    private List<Long> getPostIds(List<PostQueryDto> posts) {
        List<Long> postIds = posts.stream()
                .map(p -> p.getPostId())
                .collect(Collectors.toList());
        return postIds;
    }

    // lazy에서 초기화해서 dto로 받을 수 있으나 1 + n 쿼리 문제 생기기 때문에 처음부터 dto로 받는 것 (이것은 querydsl에서 더 단순화 가능)
    // repository가 api화 되는 문제 있음(활용도 낮아짐)
    private List<PostQueryDto> findAllPosts() {
        List<PostQueryDto> posts =
                em.createQuery(
                        "select new com.myproject.myweb.dto.post.query.PostQueryDto(p.id, c.name, w.id, w.name, p.title, p.content, p.isPublic, p.isComplete)" +
                                " from Post p" +
                        " join p.category c" +
                        " join p.writer w", PostQueryDto.class
                ).getResultList();
        return posts;
    }

    // 게시글 페이징 처리
    public List<Post> findAllWithCategoryAndPublicAndPagingByFetch(Long cateId, int offset) {
        return em.createQuery(
                "select p from Post p" +
                        " join fetch p.category c" +
                        " join fetch p.writer w" +
                        " where p.category.id =: cateId and p.isPublic = true", Post.class)
                .setParameter("cateId", cateId)
                .setFirstResult(offset)
                .setMaxResults(10)
                .getResultList();
    }



    // 특정 like count 이상인 게시글 내림차순대로
    public List<PostByLikeCountQueryDto> findAllPostsByLikeAndCategory(Long cateId){
        List<PostByLikeCountQueryDto> result = em.createQuery("select new com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto(p.id, c.name, p.title, w.name, p.isComplete, count(l))" +
                " from Like l" +
                " join l.post p" +
                " join p.category c" +
                " join p.writer w" +
                " where p.category.id =: cateId" +
                " group by l.post" +
                " having count(l.post) >= 5" + // 100개 이상 best글
                " order by count(l.post) desc", PostByLikeCountQueryDto.class)
                .setParameter("cateId", cateId)
                .getResultList();
        return result;
    }

}

