package com.myproject.myweb.repository.like.query;

import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.repository.like.LikeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class LikeQuerydslRepositoryTest {

    @Autowired LikeQuerydslRepository likeQuerydslRepository;

    @Test
    public void 쿼리확인writerIds(){
        List<Long> userIds = new ArrayList<>();
        userIds.add(6L);
        userIds.add(10L);
        Map<Long, Long> results = likeQuerydslRepository.findAllLikesByPostsWriters(userIds);

        results.keySet().forEach(r -> System.out.println("key : " + r + " value : " + results.get(r)));
    }

    @Test
    public void 쿼리확인postIds(){
        List<Long> postIds = new ArrayList<>();
        postIds.add(13L);
        likeQuerydslRepository.findAllLikesByPostsIds(postIds);
    }
}