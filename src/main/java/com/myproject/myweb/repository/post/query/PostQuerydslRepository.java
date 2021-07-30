package com.myproject.myweb.repository.post.query;

import com.myproject.myweb.domain.Post;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.myproject.myweb.domain.QLike.like;
import static com.myproject.myweb.domain.QPost.post;
import static com.myproject.myweb.domain.QCategory.category;
import static com.myproject.myweb.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
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
                .offset(offset)
                .limit(10)
                .fetch();

        return fetch;

    }

    public List<PostByLikeCountQueryDto> findAllPostsByLikeAndCategoryAndComplete(Long cateId, Boolean isComplete) {

        List<PostByLikeCountQueryDto> fetch = jpaQueryFactory.select(Projections.constructor(PostByLikeCountQueryDto.class, post.id, category.name, post.title, user.name, post.isPublic, like.count()))
                .from(post)
                .innerJoin(post.category, category)
                .innerJoin(post.writer, user)
                .innerJoin(post.likeList, like)
                .where(eqCategoryId(cateId), eqIsComplete(isComplete))
                .groupBy(like.post)
                .having(like.count().goe(5))
                .fetch();

        return fetch;
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
