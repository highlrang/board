package com.myproject.myweb.service;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.dto.category.CategoryRequestDto;
import com.myproject.myweb.dto.category.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponseDto findById(Long id){
        Category entity = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("CategoryNotExistException"));
        return new CategoryResponseDto(entity);

    }

    @Transactional
    public Long save(CategoryRequestDto categoryRequestDto){
        return categoryRepository.save(categoryRequestDto.toEntity()).getId();
    }

    public List<CategoryResponseDto> findAll(){
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public Long update(Long id, CategoryRequestDto categoryRequestDto){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("CategoryNotFoundException"));
        category.update(categoryRequestDto.getName());

        return id;
    }

    @Transactional
    public void delete(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("CategoryNotFoundException"));
        categoryRepository.delete(category);
    }

}
