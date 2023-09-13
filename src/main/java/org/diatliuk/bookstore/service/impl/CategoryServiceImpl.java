package org.diatliuk.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.category.CategoryDto;
import org.diatliuk.bookstore.exception.CategoryNotFoundException;
import org.diatliuk.bookstore.mapper.CategoryMapper;
import org.diatliuk.bookstore.model.Category;
import org.diatliuk.bookstore.repository.category.CategoryRepository;
import org.diatliuk.bookstore.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Can't find "
                                                                + "a category with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toModel(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryMapper.toModel(categoryDto);
        category.setId(id);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
