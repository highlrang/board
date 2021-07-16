package com.myproject.myweb;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.user.UserRequestDto;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.service.user.UserService;
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
public class DataInsertTest {
    @Autowired CategoryRepository categoryRepository;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired LikeRepository likeRepository;

    @Test
    public void 카테고리insert(){

        String[] names = {"Music", "Movie", "Exercise", "Food", "Reading", "Game"};

        for(int i=0; i<names.length; i++) {
            categoryRepository.save(Category.builder()
                    .name(names[i])
                    .build()
            );
        }

        List<Category> categoryList = categoryRepository.findAll();
        System.out.println(categoryList.size());

    }

    @Test @Commit
    public void 사용자insert(){

        for(int i=1; i<101; i++) {
            String email = i+"@naver.com";
            String name = i+"사용자"+i;
            userService.join(
                    UserRequestDto.builder().email(email).name(name).password("xhxh5314").build()
            );
        }

        System.out.println(userRepository.findAll().size());

    }


    @Test @Commit
    public void 게시글insert(){

        String[] categories = {"Music", "Movie", "Exercise", "Food", "Reading", "Game"};

        for(int i=1; i<49; i++) {

            String cate;
            if(i>5) {
                cate = categories[i % 5];
            }else{
                cate = categories[i];
            }

            Post post = Post.builder()
                    .title(cate + " 게시글 " + i)
                    .content(cate + "의 " + i + "번째 게시글 내용")
                    .isPublic(true)
                    .build();

            Category category = categoryRepository.findByName(cate).get();
            post.addCategory(category);

            User user = userRepository.findByEmail(i+"@naver.com").get();
            post.addWriter(user);

            postRepository.save(post);
        }
    }

    @Test @Commit
    public void 좋아요insert(){
        List<User> users = userRepository.findByEmailContains("7");
        List<Post> posts = postRepository.findAll();


        int j=2;

        for(Post p: posts){
            if (p.getCategory().getId() == 2) continue;
            if (p.getTitle().contains("2") || p.getTitle().contains("5") || p.getTitle().contains("8") || p.getTitle().contains("9")) continue;

            for(int i=0; i<users.size(); i++){
                if(i%j==0) continue;

                likeRepository.save(Like.builder()
                        .post(p)
                        .user(users.get(i))
                        .build());
            }
            j += 1;
        }

        System.out.println(likeRepository.findAll().size());
    }
}
