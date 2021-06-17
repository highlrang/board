package com.myproject.myweb.repository.user;

import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /*
    List<User> findAll();
    User findById(Long id);
    User save(User user);

    @Query 사용 가능
    */

    // list 이기에 상세한 컬렉션 필드 호출(조인) 안 함
    List<User> findAll();

    Optional<User> findByEmail(String email);
    // Optional은 null이 될 수 있는 객체를 감싼다. orElseThrow로 예외 처리
    // ToOne은 join fetch, ToMany는 api controller에 이미 만들어놓은 dto에서 fetch(in query)됨


    //query 자동 생성됨 >> 쿼리 fetch join 필요한지 확인하기
    List<User> findByRole(Role role);
}
