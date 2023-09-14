package org.diatliuk.bookstore.service;

import java.util.List;
import org.diatliuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.diatliuk.bookstore.dto.category.CategoryDto;
import org.diatliuk.bookstore.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponseDto save(CategoryDto categoryDto);

    List<CategoryResponseDto> getAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);
}
