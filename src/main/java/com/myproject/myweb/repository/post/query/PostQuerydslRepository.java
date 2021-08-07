package com.myproject.myweb.repository.post.query;

import com.myproject.myweb.domain.Post;
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

    // fetch + paging
    public List<Post> findAllWithCategoryAndPublicAndPagingByFetch(Long cateId, int offset) {
        List<Post> fetch = jpaQueryFactory.selectFrom(post)
                .innerJoin(post.category, category)
                .fetchJoin()
                .innerJoin(post.writer, user)
                .fetchJoin()
                .where(category.id.eq(cateId), post.isPublic.eq(true))
                .orderBy(post.id.desc())
                .offset(offset)
                .limit(10)
                .fetch();

        return fetch;

    }

    // mylist paging
    public List<Post> findAllWithCategoryAndWriterAndPagingByFetch(Long cateId, Long userId, int offset){
        List<Post> fetch = jpaQueryFactory.selectFrom(post)
                .innerJoin(post.category, category)
                .fetchJoin()
                .innerJoin(post.writer, user)
                .fetchJoin()
                .where(category.id.eq(cateId), user.id.eq(userId))
                .orderBy(post.id.desc())
                .offset(offset)
                .limit(10)
                .fetch();
        return fetch;
    }

    // bestlist paging
    public List<PostByLikeCountQueryDto> findAllPostsByLikeAndCategoryAndComplete(Long cateId, Boolean isComplete, int offset) {
        JPAQuery<PostByLikeCountQueryDto> jpaQuery = jpaQueryFactory.select(Projections.constructor(PostByLikeCountQueryDto.class, post.id, category.name, post.title, user.name, post.isPublic, like.count()))
                .from(post)
                .innerJoin(post.category, category)
                .innerJoin(post.writer, user)
                .innerJoin(post.likeList, like)
                .where(eqCategoryId(cateId), eqIsComplete(isComplete))
                .groupBy(like.post)
                .having(like.count().goe(5))
                .orderBy(like.count().desc());

        if(offset >= 0) { // 전체 전달일 경우 페이징 처리 ㄴㄴ
            jpaQuery.offset(offset).limit(10);
        }

        List<PostByLikeCountQueryDto> fetch = jpaQuery.fetch();
        return fetch;
    }

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
        // return (long) fetch.stream().mapToInt(Long::intValue).sum();
    }

    private BooleanExpression eqCategoryId(Long cateId){
        if(cateId == null){
            return null;
        }
        return category.id.eq(cateId);
    }

    private BooleanExpression eqIsComplete(Boolean isComplete) {
        if(isComplete == null){
            return null;
        }
        return post.isComplete.eq(isComplete);
    }
}
