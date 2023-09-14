package org.diatliuk.bookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.diatliuk.bookstore.config.MapperConfig;
import org.diatliuk.bookstore.dto.book.BookDto;
import org.diatliuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.diatliuk.bookstore.dto.book.CreateBookRequestDto;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", source = "categories")
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toBookDtoWithoutCategoryIds(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(
                    book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet())
            );
        }
    }

    default Set<Long> mapCategoriesToCategoryIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    default Set<Category> mapCategoryIdsToCategories(Set<Long> categoryIds) {
        return categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }

    @Mapping(target = "categories", source = "categoryIds")
    Book toModel(CreateBookRequestDto requestDto);
}
