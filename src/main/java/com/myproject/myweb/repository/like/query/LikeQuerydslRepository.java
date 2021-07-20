package com.myproject.myweb.repository.like.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;


}
