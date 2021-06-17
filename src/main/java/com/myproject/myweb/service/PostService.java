package com.myproject.myweb.service;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.CategoryResponseDto;
import com.myproject.myweb.dto.like.LikeResponseDto;
import com.myproject.myweb.dto.post.PostDetailResponseDto;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.dto.post.PostResponseDto;
import com.myproject.myweb.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public PostDetailResponseDto findById(Long id){
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));
        PostDetailResponseDto post = new PostDetailResponseDto(entity);

        // findById(detail view)에서는 게시글의 likeList까지 !

        Long totalLike = likeRepository.countAllByPost_Id(post.getId())
                .orElseThrow(() -> new IllegalStateException());
        post.addTotalLike(totalLike);

        List<LikeResponseDto> likes = likeRepository.findAllByPost_Id(post.getId())
                .stream()
                .map(l -> new LikeResponseDto(l))
                .collect(Collectors.toList());
        post.addLikeList(likes);

        // 여기서는 controller용으로 했지만 api에 만들어놨던 거로 연결 가능?

        return post;
    }

    @Transactional
    public Long save(PostRequestDto postRequestDto){
        // 카테고리랑 작성자 findById로 찾아서 mapping해주기
        Category category = categoryRepository.findById(postRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalStateException());
        User writer = userRepository.findById(postRequestDto.getWriterId())
                .orElseThrow(() -> new IllegalStateException());

        Post post = postRequestDto.toEntity();
        post.addCategory(category);
        post.addWriter(writer);
        return postRepository.save(post).getId();
    }

    // Long 말고 void로 하는 거는? api때문에 return id 하는 것인지
    @Transactional
    public Long update(Long id, PostRequestDto postRequestDto){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));

        post.update(postRequestDto.getTitle(), postRequestDto.getContent(), postRequestDto.getIsPublic());
        return id;
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAll(){
        return postRepository.findAll().stream()
                .map(PostResponseDto::new) // dto로 받기
                .collect(Collectors.toList()); // list화
    }

    @Transactional(readOnly=true)
    public List<PostResponseDto> findAllMine(Long cateId, Long writerId){
        return postRepository.findAllByCategory_IdAndWriter_Id(cateId, writerId)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllByCategory(Long cateId){
        // postQueryRepository로 페이징 test하기
        return postRepository.findAllByCategory_IdAndIsPublic(cateId, true)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public void delete(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));

        post.getWriter().postDelete(post);
        post.getCategory().postDelete(post);

        postRepository.delete(post);
    }

    public Long postCompleteUpdate(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));

        Boolean isComplete = !post.getIsComplete();
        post.completeUpdate(isComplete);

        return id;

    }
}
