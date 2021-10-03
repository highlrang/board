package com.myproject.myweb.repository.user.query;


import com.myproject.myweb.domain.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class UserQueryRepository {

    private final EntityManager em;

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

        em.clear(); // 초기화로 before data 제거
        // em.refresh(User.class); // 데이터 재조회

        // em.setFlushMode(FlushModeType.AUTO);
        // em.flush();
        // auto는 commit과 달리 jpql 전에도 flush 해서 변경감지 등 일관성 유지

    }
}
