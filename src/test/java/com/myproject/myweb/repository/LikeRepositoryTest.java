package com.myproject.myweb.repository;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class LikeRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private LikeRepository likeRepository;

    @Test
    public void query(){
        List<Like> likes = likeRepository.findAllByUser_Id(Long.valueOf(10));
        for(Like l :likes){
            System.out.println("회원 " + l.getUser().getName());
        }
        System.out.println(likes.size());
    }

    @Test @Commit
    public void 좋아요(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);

        likeRepository.save(Like.builder().post(post).user(user).build());

        Like like = likeRepository.findLikeOne(post.getId(), user.getId()).get();

        assertThat(like.getUser()).isEqualTo(user);
        assertThat(like.getPost()).isEqualTo(post);
    }

    @Test @Commit
    public void 좋아요_취소(){
        Like like = likeRepository.findAll().get(0);
        Long id = like.getId();
        likeRepository.delete(like);


        Boolean isPresent = likeRepository.findById(id).isPresent();
        assertThat(isPresent).isEqualTo(false);
    }

    @Test
    public void 회원별_좋아요_총개수(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();

        Long count = likeRepository.countAllByPostWriter(user.getId()).get();

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void 게시글별_좋아요_총개수(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);

        Long count = likeRepository.countAllByPost_Id(post.getId()).get();

        assertThat(count).isEqualTo(1);

    }
}
