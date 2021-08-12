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

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
        Post post = postRepository.findAllByWriterFetch(user.getId()).get(0);
        Long userId = user.getId();
        Long postId = post.getId();

        Boolean thereIs;
        Long id = -1L;
        try {
            LikeResponseDto like = likeService.findLikeOne(postId, userId);

            id = like.getId();
            thereIs = true;

        }catch (IllegalStateException e) {

            thereIs = false;

        } finally {

            likeService.push(LikeRequestDto.builder()
                    .postId(post.getId())
                    .userId(user.getId())
                    .build());

        }

        if(thereIs && id != -1L){
            assertFalse(likeRepository.findById(id).isPresent());
        }else{
            assertTrue(likeRepository.findByPost_IdAndUser_Id(postId, userId).isPresent());
        }



    }

}