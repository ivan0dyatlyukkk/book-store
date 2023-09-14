package org.diatliuk.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.diatliuk.bookstore.dto.category.CategoryDto;
import org.diatliuk.bookstore.dto.category.CategoryResponseDto;
import org.diatliuk.bookstore.exception.CategoryNotFoundException;
import org.diatliuk.bookstore.mapper.BookMapper;
import org.diatliuk.bookstore.mapper.CategoryMapper;
import org.diatliuk.bookstore.model.Category;
import org.diatliuk.bookstore.repository.book.BookRepository;
import org.diatliuk.bookstore.repository.category.CategoryRepository;
import org.diatliuk.bookstore.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toModel(categoryDto);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Can't find "
                                                                + "a category with id: " + id));
        return categoryMapper.toDto(category);
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

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategoriesId(id).stream()
                .map(bookMapper::toBookDtoWithoutCategoryIds)
                .toList();
    }
}
