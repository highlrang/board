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

    }

    @Test
    public void 좋아요(){
        User user = userRepository.findByEmail("jhw123@naver.com").get();
        Post post = postRepository.findById(Long.valueOf(14)).get(); // 운동 제목

        Like like = likeRepository.save(Like.builder().post(post).user(user).build());

        // System.out.println(like.getUser());
        assertThat(like.getUser()).isEqualTo(user);
        assertThat(like.getPost()).isEqualTo(post);
    }

    @Test
    public void 좋아요_취소(){
        Like like = likeRepository.findAll().get(0);
        Long id = like.getId();
        likeRepository.delete(like);


        Boolean isPresent = likeRepository.findById(id).isPresent();
        assertThat(isPresent).isEqualTo(false);
    }

    @Test
    public void 작성자별_좋아요_총개수(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Long count = likeRepository.countAllByPostWriter(user.getId()).get();

        List<Like> likes = likeRepository.findAll();
        Long likesCnt = 0L;
        for(Like l:likes){
            if(l.getPost().getWriter().equals(user)) likesCnt += 1;
        }

        assertThat(likesCnt).isEqualTo(count);
    }

    @Test
    public void 게시글별_좋아요_총개수(){
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Post post = postRepository.findByWriter(user).get(0);

        Long count = likeRepository.countAllByPost_Id(post.getId()).get();
        List<Like> likes = likeRepository.findAllByPost_Id(post.getId());

        assertThat(likes.size()).isEqualTo(Integer.valueOf(Math.toIntExact(count)));

    }
}
