package org.diatliuk.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.BookDto;
import org.diatliuk.bookstore.dto.CreateBookRequestDto;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
import org.diatliuk.bookstore.mapper.BookMapper;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.repository.BookRepository;
import org.diatliuk.bookstore.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book with id:" + id));
        return bookMapper.toDto(book);
    }
}
