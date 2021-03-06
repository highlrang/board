package com.myproject.myweb.repository.post;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.post.query.PostQueryRepository;
import com.myproject.myweb.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import com.myproject.myweb.domain.Post;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class PostRepositoryTest {

    @Autowired PostRepository postRepository;
    @Autowired PostQueryRepository postQueryRepository;
    @Autowired UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void 게시글_저장(){
        User writer = userRepository.findByEmail("jhw127@naver.com").get();
        Category category = categoryRepository.findByName("테스트 카테고리").get();
        String title = "test";
        String content = "test";
        Boolean isPublic = true;

        Post entity = Post.builder()
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .build();
        entity.addCategory(category);
        entity.addWriter(writer);

        Long id = postRepository.save(entity).getId();
        // category, user 도 persist 해줘야함 >> Cascade는 사용

        Post post = postRepository.findById(id).get();

        assertThat(post.getTitle()).isEqualTo(title);
    }

    @Test
    public void 게시글_수정(){
        Post post = postRepository.findAll().get(0);
        Long id = post.getId();

        String content = "내용 수정 테스트";
        Boolean isPublic = !post.getIsPublic();
        post.update(post.getTitle(), content, isPublic);

        Post updatedPost = postRepository.findById(id).get();

        assertThat(updatedPost.getContent()).isEqualTo(content);

        // isNotEmpty, contains<>, isEqualTo,
        // isnull, toString, has? ...
    }

    @Test
    public void 게시글_삭제(){
        List<Post> postList = postRepository.findAll();

        Post post = postList.get(0);
        Long id = post.getId();
        postRepository.delete(post);

        Boolean isPresent = postRepository.findById(id).isPresent();
        assertThat(isPresent).isEqualTo(false);
    }

    @Test
    public void 게시글_총개수(){
        List<Post> postList = postRepository.findAll();

        System.out.println(postList.size());
    }

    @Test
    public void 게시글_페이징(){
        Long cateId = categoryRepository.findAll().get(0).getId();
        List<Post> posts = postQueryRepository.findAllWithCategoryAndPublicAndPagingByFetch(cateId, 1);
        List<Post> second = postQueryRepository.findAllWithCategoryAndPublicAndPagingByFetch(cateId, 0);
        // offset은 시작 위치, limit은 가져올 개수


    }
}