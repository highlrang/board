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

    // left join 날림 -> select one(count) 이기에 성능 차이 없음
    Long countAllByPost_Writer(User user);
    Long countAllByPost_Id(Long postId);

    List<Like> findAllByPost_Id(Long postId);
    List<Like> findAllByUser_Id(Long userId);

}