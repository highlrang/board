package com.myproject.myweb.service;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.like.LikeRequestDto;
import com.myproject.myweb.dto.like.LikeResponseDto;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public LikeResponseDto findLikeOne(Long postId, Long userId){
        Like entity = likeRepository.findByPost_IdAndUser_Id(postId, userId)
                .orElseThrow(() -> new IllegalStateException("LikeNotFoundException"));

        return new LikeResponseDto(entity);
    }

    @Transactional
    public Long save(LikeRequestDto likeRequestDto){
        Post post = postRepository.findById(likeRequestDto.getPostId())
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));
        User user = userRepository.findById(likeRequestDto.getUserId())
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));

        return likeRepository.save(
                Like.builder()
                .post(post)
                .user(user)
                .build()
        ).getId();
    }


    @Transactional
    public void push(LikeRequestDto likeRequestDto){

        Long zero = 0L;
        Long id = likeRepository.findByPost_IdAndUser_Id(likeRequestDto.getPostId(), likeRequestDto.getUserId())
                .map(Like::getId)
                .orElse(zero);

        if(!id.equals(zero)){
            this.delete(id);

        }else{
            this.save(likeRequestDto);
        }
    }

    @Transactional
    public void delete(Long id){
        Like like = likeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("LikeNotFoundException"));

        like.getPost().likeDelete(like);
        like.getUser().likeDelete(like);

        likeRepository.delete(like);

    }

    // 특정 게시글 좋아요 개수
    public Long findLikeInPost(Long postId){
        return likeRepository.countAllByPost_Id(postId); // sum은 null, count는 0이기에 에러처리 안 함
    }

    // user가 자신의 모든 게시글(작성자)로 받은 모든 like 개수
    public Long findLikeInUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));
        return likeRepository.countAllByPost_Writer(user);

    }
}
