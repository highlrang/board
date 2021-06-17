package com.myproject.myweb.repository.like;

import com.myproject.myweb.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;



public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.post.id = :post and l.user.id = :user")
    Optional<Like> findLikeOne(@Param(value="post") Long postId, @Param("user") Long userId);

    // 전달 파라미터 하나일 때 이거 이용, 여러 개일 때 queryRepository꺼 이용
    @Query("SELECT count(l) FROM Like l WHERE l.post.writer = (SELECT u FROM User u WHERE u.id = :userId)")
    Optional<Long> countAllByPostWriter(@Param(value="userId") Long userId);

    Optional<Long> countAllByPost_Id(Long postId);

    // 자동 쿼리 생성됨
    // @Query("select l from Like l where l.user.id =:userId)
    List<Like> findAllByPost_Id(Long postId);
    List<Like> findAllByUser_Id(Long userId);

}

// table name이 아닌 damain 이름으로 해야함
