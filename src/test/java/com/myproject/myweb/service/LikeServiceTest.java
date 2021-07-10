package com.myproject.myweb.service;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.dto.like.LikeRequestDto;
import com.myproject.myweb.dto.like.LikeResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class LikeServiceTest {

    @Autowired private PostRepository postRepository;
    @Autowired private LikeService likeService;
    @Autowired private LikeRepository likeRepository;
    @Autowired private UserRepository userRepository;

    @Test
    public void 게시글_좋아요_있으면_자동삭제_테스트_push(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);
        Long userId = user.getId();
        Long postId = post.getId();

        try {
            LikeResponseDto like = likeService.findLikeOne(postId, userId);

            // id
            // 있다는 변수
            // 후에 id 해당하는 like 없는 거 확인

        }catch (IllegalStateException e) {

            // 없다는 변수
            // 후에 like 생긴 거 확인

        } finally {

            likeService.push(LikeRequestDto.builder()
                    .postId(post.getId())
                    .userId(user.getId())
                    .build());

            // 있다는 변수면은 기존 id로 확인

            // 없다는 변수면 findLike 해서 생성됐는지 보기

        }

    }

}