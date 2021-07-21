package com.myproject.myweb.repository.user.query;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.user.query.UserLikeQueryDto;
import com.myproject.myweb.dto.user.query.UserPostQueryDto;
import com.myproject.myweb.dto.user.query.UserQueryDto;
import com.myproject.myweb.dto.user.query.WriterByLikeCountQueryDto;
import com.querydsl.core.types.Projections;

import static com.myproject.myweb.domain.QLike.like;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.myproject.myweb.domain.QPost.post;
import static com.myproject.myweb.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<User> findAllByMap(){
        // select 절에 list 안 나옴. lazy 상태이기 때문에
        // for문 돌면서 postList 호출하면 user ids "in query"로 추가 query 발생
        List<User> transform = queryFactory.selectFrom(user)
                .fetch();

        return transform;
    }

    public List<User> findAllByFetch(){ // n+1해결
        List<User> fetch = queryFactory.selectFrom(user)
                .leftJoin(user.likeList) // user가 좋아한 좋아요 리스트 select절에서 가져옴(쿼리 한 번 발생)
                // 하위의 하위 엔티티도 한 줄로 가능
                // but ToMany관계는 1개만 가능 >> toOne만 fetch로 하고 나머지는 batch inQuery로
                .fetchJoin()
                .fetch();

        return fetch;
    }

    public Map<Long, UserQueryDto> findAllInQeury(){
        Map<Long, UserQueryDto> users = queryFactory.selectFrom(user)
                .transform(groupBy(user.id).as(Projections.constructor(UserQueryDto.class, user.id, user.email, user.name, user.role)));

        // keySet으로 postList 불러오기(쿼리 수 단축)
        Map<Long, List<UserPostQueryDto>> posts = queryFactory.selectFrom(post)
                .where(post.writer.id.in(users.keySet()))
                .transform(groupBy(post.writer.id).as(list(Projections.constructor(UserPostQueryDto.class, post.writer.id, post.id, post.category.name, post.title, post.content, post.isPublic, post.isComplete))));

        List<Like> likes = queryFactory.selectFrom(like)
                // cross join 되기에 fetch join 해야함 >> entity로 반환
                .innerJoin(like.user, user)
                .fetchJoin()
                .innerJoin(like.post, post)
                .fetchJoin()
                .innerJoin(post.category)
                .fetchJoin()
                .innerJoin(post.writer)
                .fetchJoin()
                .where(user.id.in(users.keySet()))
                .fetch();

        // transform(groupBy(like.user.id).as(list(Projections.constructor(UserLikeQueryDto.class, like.user.id, like.post.id, like.post.writer.name, like.post.category.name, like.post.title, like.post.isComplete))));
        // new 생성자 또는 stream dto 변환
        Map<Long, List<UserLikeQueryDto>> likesSort = likes.stream()
                .map(l -> new UserLikeQueryDto(l.getUser().getId(), l.getPost().getId(), l.getPost().getWriter().getName(), l.getPost().getCategory().getName(), l.getPost().getTitle(), l.getPost().getIsComplete()))
                .collect(Collectors.groupingBy(UserLikeQueryDto::getUserId));

        for (Long id: users.keySet()) {
            users.get(id).addUserPosts(posts.get(id));
            users.get(id).addUserLikes(likesSort.get(id));
        }

        return users;

    }

    public List<WriterByLikeCountQueryDto> findAllWritersByLikeCount(Long count){

        List<WriterByLikeCountQueryDto> writers = queryFactory
                .select(Projections.constructor(WriterByLikeCountQueryDto.class, user.id, user.role, user.name, user.email, like.count()))
                .from(post)
                .leftJoin(post.writer, user)
                .leftJoin(post.likeList, like)
                .groupBy(user)
                .having(like.count().goe(count))
                .fetch();

        return writers;
    }
}
