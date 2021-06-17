package com.myproject.myweb.scheduler;


import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.dto.user.query.WriterByLikeCountQueryDto;
import com.myproject.myweb.repository.post.query.PostQueryRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.repository.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerTask {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    // n1 이상의 like인 NORMAL유저를 승급하는 메서드 > n2 이상의 silval 승급은 다른 메서드에
    // @Async
    @Scheduled(cron = "* * 9 15 * *") // "0 */1 * * * *"
    public void roleUpdateTask1(){
        List<WriterByLikeCountQueryDto> users =
                userQueryRepository.findAllWritersByLikeCount(Long.valueOf(4));

        // 변경감지는 entity로만 가능 >> total like count와 role에 따라 update 쿼리 생성해서 update할 것
        List<Long> result = users.stream()
                .filter(u -> u.getUserRole() == Role.NORMAL_USER.getTitle())
                .map(u -> u.getUserId())
                .collect(Collectors.toList());

        Long id = result.get(0);

        // + 승급 알림 보내기
        userQueryRepository.updateUserRole(result, Role.SILVAL_USER);
        log.info(String.valueOf(userRepository.findById(id).get().getRole()));
    }

    /*
    @Scheduled(cron = "* * * * * *")
    public void PostCompleteTask1(){
        List<PostByLikeCountQueryDto> posts =
                postQueryRepository.findAllPostsByLikeAndComplete(Long.valueOf(5));

        // 변경감지는 entity로만 가능 >> 해결 테이블에 dto를 entity로 바꿔서 전달(save)하기
        // completeInterfaceRepository.saveAll(List<Complete> entityList);
    }
    */
}

