package com.myproject.myweb.repository.post.query;

import com.myproject.myweb.domain.Post;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.repository.post.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PostQuerydslRepositoryTest {

    @Autowired
    PostQuerydslRepository postQuerydslRepository;

    @Test
    public void 쿼리(){
        // 페이징 확인
        postQuerydslRepository.findAllPaging(2L, null, true, 0);

        List<PostByLikeCountQueryDto> posts = postQuerydslRepository.findAllPostsByLike(2L, null, 0);
        posts.forEach(p -> System.out.println(p.getTitle() + p.getIsComplete()));
    }
}