package com.myproject.myweb.repository;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class) // 꼭 필요???
@SpringBootTest // 꼭 필요???
@Transactional
public class CategoryRepositoryTest{
    @Autowired CategoryRepository categoryRepository;
    @Autowired PostRepository postRepository;

    @Test @Commit
    public void 카테고리_생성(){

        String name = "테스트 카테고리";
        categoryRepository.save(Category.builder()
                .name(name)
                .build()
        );


        List<Category> categoryList = categoryRepository.findAll();
        Category category = categoryList.get(0);

        System.out.println(category.getPostList());
        assertThat(category.getName()).isEqualTo(name);

    }

    @Test
    public void 카테고리_수정(){

        List<Category> categoryList = categoryRepository.findAll();
        Category category = categoryList.get(0);
        Long cateId = category.getId();

        String name = "카테고리 수정 테스트";
        category.update(name);

        Category updatedCategory = categoryRepository.findById(cateId).get();
        assertThat(updatedCategory.getName()).isEqualTo(name);
    }

    @Test
    public void 카테고리_삭제(){

        List<Category> categoryList = categoryRepository.findAll();
        Category category = categoryList.get(0);
        Long cateId = category.getId();

        categoryRepository.delete(category);

        Boolean isPresent = categoryRepository.findById(cateId).isPresent();
        List<Post> posts = postRepository.findAll();

        assertThat(isPresent).isEqualTo(false);
        assertThat(posts.size()).isEqualTo(0);


    }





}