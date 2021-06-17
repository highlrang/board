package com.myproject.myweb.repository;

import com.myproject.myweb.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name); // >> 후에는 List<Category>로 받아야함 !!!!
}
