package com.myproject.myweb.service;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.dto.like.LikeRequestDto;
import com.myproject.myweb.dto.like.LikeResponseDto;
import org.junit.jupiter.api.Test;
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


    @Test //@Commit
    public void 게시글_좋아요(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);

        likeService.save(LikeRequestDto.builder()
                .postId(post.getId())
                .userId(user.getId())
                .build());


        Like like = likeRepository.findAll().get(0);
        Long postCount = likeRepository.countAllByPost_Id(post.getId()).get();
        Long userCount = likeRepository.countAllByPostWriter(user.getId()).get();

        assertThat(user.getLikeList().contains(like)).isEqualTo(true);
        assertThat(post.getLikeList().contains(like)).isEqualTo(true);
        // assertThat(postCount).isEqualTo();
        // assertThat(userCount).isEqualTo();
    }

    @Test
    public void 좋아요_삭제(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        User writer = userRepository.findByEmail("jhw123@naver.com").get();
        Post post = postRepository.findByWriter(writer).get(0);
        Like like = likeRepository.findLikeOne(post.getId(), user.getId()).get();

        likeService.delete(like.getId());

        assertThat(user.getLikeList().contains(like)).isEqualTo(false);
        assertThat(post.getLikeList().contains(like)).isEqualTo(false);

        // likeRepository.findLikeCountWithPost(post.getId()).get();
        // likeRepository.findLikeCountWithUser(user.getId()).get();
    }

    @Test
    public void 게시글_좋아요_있으면_자동삭제_테스트_push(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);
        Long userId = user.getId();
        Long postId = post.getId();

        try {
            likeService.findLikeOne(postId, userId);

            likeService.push(LikeRequestDto.builder()
                    .postId(post.getId())
                    .userId(user.getId())
                    .build());

            assertThrows(IllegalArgumentException.class,
                    () -> likeService.findLikeOne(postId, userId));


        }catch(IllegalArgumentException e){

        }

    }

    @Test
    public void 게시글_총좋아요수(){
        // like save 되었을 때 user와 post like count 증가하는지 확인
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);

        Long totalLikeUser = likeRepository.countAllByPostWriter(user.getId()).get();
        Long totalLikePost = likeRepository.countAllByPost_Id(post.getId()).get();


        likeService.save(LikeRequestDto.builder()
                .postId(post.getId())
                .userId(user.getId())
                .build()
        );

        Long totalLikeUserAfter = likeRepository.countAllByPostWriter(user.getId()).get();
        Long totalLikePostAfter = likeRepository.countAllByPost_Id(post.getId()).get();


        assertThat(totalLikePostAfter).isEqualTo(totalLikePost + 1);
        assertThat(totalLikeUserAfter).isEqualTo(totalLikeUser + 1);

    }

    @Test
    public void 게시글_총좋아요수_취소(){
        // like delete 되었을 때 post like count 감소하는지 확인
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);
        Long totalLike = likeRepository.countAllByPost_Id(post.getId()).get();

        LikeResponseDto like = likeService.findLikeOne(post.getId(), user.getId());

        likeService.delete(like.getId());

        Long totalLikeAfter = likeRepository.countAllByPost_Id(post.getId()).get();


        assertThat(totalLikeAfter).isNotEqualTo(totalLike);
        assertThat(post.getLikeList().size()).isEqualTo(0);
    }

    @Test @Commit
    public void 회원_총좋아요수(){
        // like save 되었을 때 user totalLike 증가하는지 확인
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);

        Long totalLike = likeRepository.countAllByPostWriter(user.getId()).get();



        likeService.save(LikeRequestDto.builder()
                .postId(post.getId())
                .userId(user.getId())
                .build());

        Long totalLikeAfter = likeRepository.countAllByPostWriter(user.getId()).get();

        assertThat(totalLikeAfter).isNotEqualTo(totalLike);
        assertThat(post.getLikeList().size()).isEqualTo(0);

    }

    @Test
    public void 회원_총좋아요수_취소(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();

        Like like = likeRepository.findAll().get(0);

        Long totalLike = likeRepository.countAllByPostWriter(user.getId()).get();


        likeService.delete(like.getId());

        Long totalLikeAfter = likeRepository.countAllByPostWriter(user.getId()).get();


        // then
        assertThat(totalLikeAfter).isEqualTo(totalLike-1);
        //assertThat(user.getLikeList().size()).isEqualTo(0);
    }

}