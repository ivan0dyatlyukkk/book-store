package org.diatliuk.bookstore.service;

import java.util.List;
import org.diatliuk.bookstore.dto.BookDto;
import org.diatliuk.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto getById(Long id);
}
