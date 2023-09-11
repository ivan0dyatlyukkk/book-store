package org.diatliuk.bookstore.mapper;

import org.diatliuk.bookstore.config.MapperConfig;
import org.diatliuk.bookstore.dto.book.BookDto;
import org.diatliuk.bookstore.dto.book.CreateBookRequestDto;
import org.diatliuk.bookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
