package org.diatliuk.bookstore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.diatliuk.bookstore.dto.book.BookDto;
import org.diatliuk.bookstore.dto.book.CreateBookRequestDto;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
import org.diatliuk.bookstore.mapper.BookMapper;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.model.Category;
import org.diatliuk.bookstore.repository.book.BookRepository;
import org.diatliuk.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private static final CreateBookRequestDto CREATE_BOOK_REQUEST_DTO = new CreateBookRequestDto();
    private static final BookDto RESPONSE_BOOK_DTO = new BookDto();
    private static final Book TEST_BOOK = new Book();
    private static final Category TEST_CATEGORY = new Category();
    private static final Long INVALID_BOOK_ID = -1L;
    private static final Pageable PAGEABLE = Pageable.ofSize(10);

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    static void beforeAll() {
        TEST_CATEGORY.setId(1L);
        TEST_CATEGORY.setName("Fiction");
        TEST_CATEGORY.setDescription("Fiction books");
        TEST_CATEGORY.setDeleted(false);

        CREATE_BOOK_REQUEST_DTO.setTitle("Test book 1");
        CREATE_BOOK_REQUEST_DTO.setAuthor("Tester 1");
        CREATE_BOOK_REQUEST_DTO.setIsbn("978-0-06-251140-9");
        CREATE_BOOK_REQUEST_DTO.setPrice(BigDecimal.valueOf(200));
        CREATE_BOOK_REQUEST_DTO.setDescription("This is a description");
        CREATE_BOOK_REQUEST_DTO.setCoverImage("link.ua//image1");
        CREATE_BOOK_REQUEST_DTO.setCategoryIds(Set.of(TEST_CATEGORY.getId()));

        TEST_BOOK.setId(1L);
        TEST_BOOK.setTitle(CREATE_BOOK_REQUEST_DTO.getTitle());
        TEST_BOOK.setAuthor(CREATE_BOOK_REQUEST_DTO.getAuthor());
        TEST_BOOK.setIsbn(CREATE_BOOK_REQUEST_DTO.getIsbn());
        TEST_BOOK.setPrice(CREATE_BOOK_REQUEST_DTO.getPrice());
        TEST_BOOK.setDescription(CREATE_BOOK_REQUEST_DTO.getDescription());
        TEST_BOOK.setCoverImage(CREATE_BOOK_REQUEST_DTO.getCoverImage());
        TEST_BOOK.setDeleted(false);
        TEST_BOOK.setCategories(Set.of(TEST_CATEGORY));

        RESPONSE_BOOK_DTO.setId(TEST_BOOK.getId());
        RESPONSE_BOOK_DTO.setTitle(TEST_BOOK.getTitle());
        RESPONSE_BOOK_DTO.setAuthor(TEST_BOOK.getAuthor());
        RESPONSE_BOOK_DTO.setIsbn(TEST_BOOK.getIsbn());
        RESPONSE_BOOK_DTO.setPrice(TEST_BOOK.getPrice());
        RESPONSE_BOOK_DTO.setDescription(TEST_BOOK.getDescription());
        RESPONSE_BOOK_DTO.setCoverImage(TEST_BOOK.getCoverImage());
        RESPONSE_BOOK_DTO.setCategoryIds(CREATE_BOOK_REQUEST_DTO.getCategoryIds());
    }

    @Test
    @DisplayName("""
        Verify save() method by saving a book with correct data
    """)
    void save_withValidData_returnsValidBook() {
        when(bookMapper.toModel(CREATE_BOOK_REQUEST_DTO)).thenReturn(TEST_BOOK);
        when(bookRepository.save(TEST_BOOK)).thenReturn(TEST_BOOK);
        when(bookMapper.toDto(TEST_BOOK)).thenReturn(RESPONSE_BOOK_DTO);

        BookDto actualBookDto = bookService.save(CREATE_BOOK_REQUEST_DTO);

        assertEquals(RESPONSE_BOOK_DTO, actualBookDto);
    }

    @Test
    @DisplayName("""
        Verify getAll() method with quantity restrictions
    """)
    void getAll_withPagination_returnsValidNumberOfBooks() {
        when(bookRepository.findAll(PAGEABLE)).thenReturn(new PageImpl<>(List.of(TEST_BOOK)));
        when(bookMapper.toDto(any())).thenReturn(RESPONSE_BOOK_DTO);

        List<BookDto> actualAllBooks = bookService.getAll(PAGEABLE);

        assertEquals(List.of(RESPONSE_BOOK_DTO), actualAllBooks);
    }

    @Test
    @DisplayName("""
        Verify getById() method by using a valid id
    """)
    void getById_withValidId_returnsBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(TEST_BOOK));
        when(bookMapper.toDto(TEST_BOOK)).thenReturn(RESPONSE_BOOK_DTO);

        BookDto actualBook = bookService.getById(TEST_BOOK.getId());

        assertEquals(RESPONSE_BOOK_DTO, actualBook);
    }

    @Test
    @DisplayName("""
        Verify getById() method by using a valid id
    """)
    void getById_withInvalidId_throwsException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getById(INVALID_BOOK_ID));
    }

    @Test
    @DisplayName("""
        Verify update() method by using an existing book
    """)
    void update_withValidId_returnsUpdatedBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(TEST_BOOK));
        when(bookMapper.toModel(any())).thenReturn(TEST_BOOK);
        when(bookMapper.toDto(any())).thenReturn(RESPONSE_BOOK_DTO);

        BookDto updatedBook = bookService.update(TEST_BOOK.getId(), CREATE_BOOK_REQUEST_DTO);

        assertEquals(RESPONSE_BOOK_DTO, updatedBook);
    }

    @Test
    @DisplayName("""
        Verify update() method by using not existing book
    """)
    void update_withValidId_throwsException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.update(INVALID_BOOK_ID,
                                                                        CREATE_BOOK_REQUEST_DTO));
    }

    @Test
    @DisplayName("""
        Verify delete() method by using an existing id
    """)
    void deleteById_withValidId_void() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(TEST_BOOK));

        assertDoesNotThrow(() -> bookService.deleteById(TEST_BOOK.getId()));
    }

    @Test
    @DisplayName("""
        Verify delete() method by using not existing id
    """)
    void deleteById_withValidId_throwsException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(INVALID_BOOK_ID));
    }
}