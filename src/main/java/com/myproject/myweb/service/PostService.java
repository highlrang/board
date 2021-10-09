package com.myproject.myweb.service;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.post.PostDetailResponseDto;
import com.myproject.myweb.dto.post.query.admin.PostAdminDto;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import com.myproject.myweb.exception.ArgumentException;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.dto.post.PostResponseDto;
import com.myproject.myweb.repository.post.query.PostQuerydslRepository;
import com.myproject.myweb.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostQuerydslRepository postQuerydslRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final WebClient webClient = WebClient.create();


    public PostDetailResponseDto findById(Long id) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));
        // fetch join으로 likeList추가, 여러 개의 게시글일 경우 in 쿼리 따로 날림
        return new PostDetailResponseDto(entity);
    }

    @Transactional
    public Long save(PostRequestDto postRequestDto) {
        Category category = categoryRepository.findById(postRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalStateException("CategoryNotFoundException"));
        User writer = userRepository.findById(postRequestDto.getWriterId())
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));

        Post post = postRequestDto.toEntity();
        post.addCategory(category);
        post.addWriter(writer);
        return postRepository.save(post).getId();
    }

    private List<PostResponseDto> toPostResponseDto(List<Post> posts){
        return posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PostResponseDto> findAllMine(Long writerId){ // offset -1 전달하면 페이징 처리 안 함
        List<Post> posts = postQuerydslRepository.findAllPaging(null, writerId, null, 10);
        return toPostResponseDto(posts);
    }

    public List<PostResponseDto> findAll(){
        List<Post> posts = postQuerydslRepository.findAllPaging(null, null, true, 10);
        return toPostResponseDto(posts);
    }

    public List<PostResponseDto> findAllMineByCategory(Long cateId, Long writerId) {
        List<Post> posts = postQuerydslRepository.findAllPaging(cateId, writerId, null, 10);
        return toPostResponseDto(posts);
    }

    public List<PostResponseDto> findAllByCategory(Long cateId) {
        List<Post> posts = postQuerydslRepository.findAllPaging(cateId, null, true, 10);
        return toPostResponseDto(posts);
    }

    @Transactional
    public Long update(Long postId, PostRequestDto postRequestDto, UserResponseDto user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));

        checkWriterAuthority(post.getWriter().getId(), user);

        post.update(postRequestDto.getTitle(), postRequestDto.getContent(), postRequestDto.getIsPublic());
        return postId;
    }

    @Transactional
    public void delete(Long postId, UserResponseDto user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));

        checkWriterAuthority(post.getWriter().getId(), user);

        post.getWriter().postDelete(post);
        post.getCategory().postDelete(post);

        postRepository.delete(post);
    }

    private void checkWriterAuthority(Long writerId, UserResponseDto user){
        if(!user.getRoleTitle().equals(Role.ADMIN.getTitle()) && !user.getId().equals(writerId)) {
            throw new ArgumentException("NotTheWriterException");
        }
    }

    @Transactional
    public Long postCompleteUpdate(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));

        Boolean isComplete = !post.getIsComplete();
        post.completeUpdate(isComplete);

        return id;

    }

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    public List<PostAdminDto> postMatchingAdmin() {
        Map<String, List<Long>> postIds = getPostIdMap();

        List<PostAdminDto> postAdminDtos = getPostAdminDtos();

        for(PostAdminDto dto: postAdminDtos){
            if(postIds.containsKey(dto.getCategory())){
                dto.addPostIds(postIds.get(dto.getCategory()));
            }
        }

        return postAdminDtos;

    }

    private List<PostAdminDto> getPostAdminDtos() throws WebClientResponseException {
        // @Async public return void, completableFuture & 같은 인스턴스 안의 메서드끼리 호출할때는 비동기 호출이 되지 않는다.
        // 비동기는 async rest template >> return listenablefuture<ResoibseEntity<T>>

        String url = "http://127.0.0.1:9090/external/api/v1/members/post-admin";
        Mono<PostAdminDto[]> response = webClient.mutate()
                .baseUrl(url)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build()
                .get()
                .retrieve()
                .bodyToMono(PostAdminDto[].class);

        List<PostAdminDto> postAdmins = Arrays.asList(response.block());
        return postAdmins;
    }

    private Map<String, List<Long>> getPostIdMap() {
        List<PostByLikeCountQueryDto> allPosts =
                postQuerydslRepository.findAllPostsByLike(null, false, -1);
        allPosts.sort(Comparator.comparing(PostByLikeCountQueryDto::getLikeCount));

        // category로 grouping
        Map<String, List<Long>> postIdsByCategory = new HashMap<>();
        allPosts.stream()
                .collect(Collectors.groupingBy(PostByLikeCountQueryDto::getCategory))
                .forEach((key, value) -> postIdsByCategory.put(key, value.stream().map(PostByLikeCountQueryDto::getId).collect(Collectors.toList())));

        return postIdsByCategory;
    }

}
