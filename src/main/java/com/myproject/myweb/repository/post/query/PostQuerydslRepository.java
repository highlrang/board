package com.myproject.myweb.repository.post.query;

import com.myproject.myweb.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // public List<Post> findAllWithCategoryAndPublicAndPagingByFetch(Long cateId, int offset) {}

    // public List<PostByLikeCountQueryDto> findAllPostsByLikeAndCategory(Long cateId) {}
}
