package com.myproject.myweb.repository.user.query;


import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.dto.user.query.UserLikeQueryDto;
import com.myproject.myweb.dto.user.query.UserPostQueryDto;
import com.myproject.myweb.dto.user.query.UserQueryDto;
import com.myproject.myweb.dto.user.query.WriterByLikeCountQueryDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Repository
@RequiredArgsConstructor
@Slf4j
public class UserQueryRepository {

    private final EntityManager em;

    public List<UserQueryDto> findAllUsersByDto(){
        List<UserQueryDto> users = findAllUsers();

        List<Long> userIds = getUserIds(users);

        Map<Long, List<UserLikeQueryDto>> userLikeMap = findUserLikes(userIds);
        Map<Long, List<UserPostQueryDto>> userPostMap = findUserPosts(userIds);

        users.forEach(u -> u.addUserLikes(userLikeMap.get(u.getUserId())));
        users.forEach(u -> u.addUserPosts(userPostMap.get(u.getUserId())));

        return users;
    }

    private Map<Long, List<UserPostQueryDto>> findUserPosts(List<Long> userIds) {
        List<UserPostQueryDto> userPosts = em.createQuery("select new com.myproject.myweb.dto.user.query.UserPostQueryDto(w.id, p.id, c.name, p.title, p.content, p.isPublic, p.isComplete)"+
                " from Post p" +
                " join p.writer w" +
                " join p.category c" +
                " where p.writer.id in :userIds", UserPostQueryDto.class
                )
                .setParameter("userIds", userIds)
                .getResultList();

        Map<Long, List<UserPostQueryDto>> result = userPosts.stream()
                .collect(Collectors.groupingBy(UserPostQueryDto::getUserId));

        return result;
    }

    private Map<Long, List<UserLikeQueryDto>> findUserLikes(List<Long> userIds) {

        List<UserLikeQueryDto> userLikes = em.createQuery("select new com.myproject.myweb.dto.user.query.UserLikeQueryDto(u.id, p.id, w.name, c.name, p.title, p.isComplete)" +
                " from Like l" +
                " join l.user u" +
                " join l.post p" +
                " join p.writer w" +
                " join p.category c" +
                " where l.user.id in :userIds", UserLikeQueryDto.class) // where u.id 가 아닌 l.user.id인 거 맞는지 확인하기
                .setParameter("userIds", userIds)
                .getResultList();

        Map<Long, List<UserLikeQueryDto>> result = userLikes.stream()
                .collect(Collectors.groupingBy(UserLikeQueryDto::getUserId));

        return result;

    }

    private List<Long> getUserIds(List<UserQueryDto> users) {
        List<Long> userIds = users.stream()
                .map(u -> u.getUserId())
                .collect(Collectors.toList());
        return userIds;
    }

    private List<UserQueryDto> findAllUsers() {
        List<User> users = em.createQuery(
                "select u from User u", User.class)
                .getResultList();

        List<UserQueryDto> result = users.stream()
                .map(u -> new UserQueryDto(u.getId(), u.getEmail(), u.getName(), u.getRole()))
                .collect(Collectors.toList());

        return result;
    }


    // role 별로 분리된 user list
    public List<UserQueryDto> findAllUsersByDto(Role role){
        List<UserQueryDto> users = findAllUsersByRole(role);
        System.out.println(users.get(0).getUserRole());

        List<Long> userIds = getUserIds(users);
        Map<Long, List<UserLikeQueryDto>> userLikeMap = findUserLikes(userIds);
        Map<Long, List<UserPostQueryDto>> userPostMap = findUserPosts(userIds);

        users.forEach(p -> p.addUserLikes(userLikeMap.get(p.getUserId())));
        users.forEach(p -> p.addUserPosts(userPostMap.get(p.getUserId())));

        return users;

    }

    private List<UserQueryDto> findAllUsersByRole(Role role) {
        // fetch join할 것도 없고, User entity만 있기 때문에 dto아닌 entity로 뽑음 >> 후에 dto로 변환
        List<User> users = em.createQuery("select u" +
                " from User u" +
                " where u.role =:role"
                , User.class)
                .setParameter("role", role)
                .getResultList();

        List<UserQueryDto> result = users.stream()
                .map(u -> new UserQueryDto(u.getId(), u.getEmail(), u.getName(), u.getRole()))
                .collect(Collectors.toList());

        return result;
    }


    public List<WriterByLikeCountQueryDto> findAllWritersByLikeCount(Long count){

        List<WriterByLikeCountQueryDto> result = em.createQuery("select new com.myproject.myweb.dto.user.query.WriterByLikeCountQueryDto(w.id, w.role, w.email, w.name, count(l))" +
                " from Like l" +
                " join l.post p" + // fetch는 dto라 불가능
                " join p.writer w" +
                " group by l.post.writer" + // 이게 안될 수 있음
                " having count(l.post.writer) >= :count" +
                " order by count(l.post.writer) desc", WriterByLikeCountQueryDto.class)
                .setParameter("count", count)
                .getResultList();

        return result;

    }

    @Transactional
    public void updateUserRole(List<Long> ids, Role role){
        // 벌크연산 - 영속성 컨텍스트와 2차 캐시 무시하고 DB 직접 접근 >> 초기화 필요
        // (영속성에 있을 경우 자동으로 플러쉬, 일관성 유지)

        int count = em.createQuery("update User u" +
                        " set u.role =: role" +
                        " where u.id in :ids")
                .setParameter("role", role)
                .setParameter("ids", ids)
                .executeUpdate(); // 개수 반환

        log.info(String.valueOf(count));

        em.clear(); // em.refresh(User.class); // 초기화로 일단?

        // em.flush() em.setFlushMode(FlushModeType.AUTO);
        // auto는 commit과 달리 jpql 전에도 flush 해서 변경감지 등 일관성 유지

    }
}
