package com.myproject.myweb.repository.like.query;

import com.myproject.myweb.domain.Like;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.myproject.myweb.domain.QPost.post;
import static com.myproject.myweb.domain.QLike.like;
import static com.myproject.myweb.domain.user.QUser.user;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LikeQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public Map<Long, Long> findAllLikesByPostsWriters(List<Long> userIds){ // select like's count
        List<Tuple> fetch = jpaQueryFactory.select(user.id, like.count())
                .from(like)
                .join(like.post, post)
                .join(post.writer, user)
                .where(user.id.in(userIds))
                .groupBy(user.id)
                .fetch();

        Map<Long, Long> collect = fetch.stream().collect(Collectors.toMap(f -> f.get(user.id), f -> f.get(like.count())));

        return collect;

    }

    public Map<Long, Long> findAllLikesByPostsIds(List<Long> postIds){
        List<Tuple> fetch = jpaQueryFactory.select(post.id, like.count())
                .from(like)
                .innerJoin(like.user, user)
                .innerJoin(like.post, post)
                .where(post.id.in(postIds))
                .groupBy(post.id)
                .fetch();

        Map<Long, Long> collect = fetch.stream().collect(Collectors.toMap(f -> f.get(post.id), f -> f.get(like.count())));
        return collect;
    }

}
