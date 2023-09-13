package org.diatliuk.bookstore.service;

import java.util.List;
import org.diatliuk.bookstore.dto.category.CategoryDto;
import org.diatliuk.bookstore.model.Category;

public interface CategoryService {
    List<Category> getAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
