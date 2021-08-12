package com.myproject.myweb.repository.post.query;

import com.myproject.myweb.domain.Post;
import com.myproject.myweb.dto.post.PostResponseDto;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.myproject.myweb.domain.QLike.like;
import static com.myproject.myweb.domain.QPost.post;
import static com.myproject.myweb.domain.QCategory.category;
import static com.myproject.myweb.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // fetch & all list paging & my list paging
    public List<Post> findAllPaging(Long cateId, Long userId, Boolean isPublic, int offset) {
        List<Post> fetch = jpaQueryFactory.selectFrom(post)
                .innerJoin(post.category, category)
                .fetchJoin()
                .innerJoin(post.writer, user)
                .fetchJoin()
                .where(eqCategoryId(cateId), eqIsPublic(isPublic), eqUserId(userId))
                .orderBy(post.id.desc())
                .offset(offset)
                .limit(10)
                .fetch();

        return fetch;

    }

    // bestlist all or paging
    public List<PostByLikeCountQueryDto> findAllPostsByLike(Long cateId, Boolean isComplete, int offset) {
        JPAQuery<PostByLikeCountQueryDto> jpaQuery = jpaQueryFactory.select(Projections.constructor(PostByLikeCountQueryDto.class, post.id, category.name, post.title, user.name, post.isPublic, like.count()))
                .from(post)
                .innerJoin(post.category, category)
                .innerJoin(post.writer, user)
                .innerJoin(post.likeList, like)
                .where(eqCategoryId(cateId), eqIsComplete(isComplete))
                .groupBy(like.post)
                .having(like.count().goe(5))
                .orderBy(like.count().desc());

        if(offset >= 0) { // 전체 데이터 전달일 경우 페이징 처리 ㄴㄴ
            jpaQuery.offset(offset).limit(10);
        }

        List<PostByLikeCountQueryDto> fetch = jpaQuery.fetch();
        return fetch;
    }

    /*
    페이징 관련 데이터
        총 데이터 개수, 한 페이지에 보여질 데이터 개수,
        총 페이지 개수, 한 페이지에 보여질 페이지 개수,
        시작점은 == 현재 페이지 번호 * 한 페이지의 데이터 개수
        끝점은 == 시작점 + 한 페이지의 데이터 개수
        이전 페이지는 == (첫 번째 페이지 - 한 페이지에 보여질 페이지 개수)
                        >> 한 페이지에 보여질 페이지 개수만큼 +1
        다음 페이지는 == (마지막 페이지 + 1)
                        >> 한 페이지에 보열질 개수만큼 + 1
                           but 총 페이지 개수에 도달하면 멈춤
     */

    public Long countBestPosts(Long cateId){
        List<Post> fetch = jpaQueryFactory.selectFrom(post)
                .innerJoin(post.category, category)
                .fetchJoin()
                .innerJoin(post.likeList, like)
                .fetchJoin()
                .where(eqCategoryId(cateId))
                .groupBy(like.post)
                .having(like.count().goe(5))
                .fetch();

        return (long) fetch.size();
    }

    private BooleanExpression eqCategoryId(Long cateId){
        if(cateId == null) return null;

        return category.id.eq(cateId);
    }

    private BooleanExpression eqIsComplete(Boolean isComplete) {
        if(isComplete == null) return null;
        return post.isComplete.eq(isComplete);
    }

    private BooleanExpression eqUserId(Long userId){
        if(userId == null) return null;
        return user.id.eq(userId);
    }

    private BooleanExpression eqIsPublic(Boolean isPublic){
        if(isPublic == null) return null;
        return post.isPublic.eq(isPublic);
    }
}
