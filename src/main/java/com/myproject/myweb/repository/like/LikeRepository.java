package com.myproject.myweb.repository.like;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;



public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPost_IdAndUser_Id(Long postId, Long userId);
    // 얘는 left join 날림 user, post 정보 쓸 거면 fetch 등 조치 필요

    // 전달 파라미터 하나일 때
    // @Query("SELECT count(l) FROM Like l WHERE l.post.writer = (SELECT u FROM User u WHERE u.id = :userId)")
    Long countAllByPost_Writer(User user); // id가 아닌 writer 전달
    // left join 날림 but select one(count) 이기에 성능 차이 없음
    Long countAllByPost_Id(Long postId);

    // 자동 쿼리 생성
    // @Query("select l from Like l where l.user.id =:userId)
    List<Like> findAllByPost_Id(Long postId);
    List<Like> findAllByUser_Id(Long userId);

}

// table name이 아닌 damain(entitiy) 이름으로 해야함
