package org.diatliuk.bookstore.mapper;

import org.diatliuk.bookstore.config.MapperConfig;
import org.diatliuk.bookstore.dto.category.CategoryDto;
import org.diatliuk.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryDto categoryDto);
}
