package com.myproject.myweb.repository.like;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;



public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.post.id = :post and l.user.id = :user")
    Optional<Like> findLikeOne(@Param(value="post") Long postId, @Param("user") Long userId);
    Optional<Like> findByPost_IdAndUser_Id(Long postId, Long userId); // 이거 같은 쿼리 날리는지 확인


    // 전달 파라미터 하나일 때 이거 이용, 여러 개일 때 queryRepository꺼 이용
    // @Query("SELECT count(l) FROM Like l WHERE l.post.writer = (SELECT u FROM User u WHERE u.id = :userId)")
    Optional<Long> countAllByPost_Writer(User user); // id가 아닌 writer 전달하기
    Optional<Long> countAllByPost_Id(Long postId);

    // 자동 쿼리 생성
    // @Query("select l from Like l where l.user.id =:userId)
    List<Like> findAllByPost_Id(Long postId);
    List<Like> findAllByUser_Id(Long userId);

}

// table name이 아닌 damain(entitiy) 이름으로 해야함
