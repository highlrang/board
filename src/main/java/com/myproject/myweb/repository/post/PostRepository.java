package com.myproject.myweb.repository.post;

import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;


public interface PostRepository extends JpaRepository<Post, Long>{
    Long countByCategory_Id(Long cateId);
    Long countByCategory_IdAndWriter_Id(Long cateId, Long writerId);
}