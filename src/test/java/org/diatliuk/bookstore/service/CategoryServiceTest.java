package org.diatliuk.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.diatliuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.diatliuk.bookstore.dto.category.CategoryDto;
import org.diatliuk.bookstore.dto.category.CategoryResponseDto;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
import org.diatliuk.bookstore.mapper.BookMapper;
import org.diatliuk.bookstore.mapper.CategoryMapper;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.model.Category;
import org.diatliuk.bookstore.repository.book.BookRepository;
import org.diatliuk.bookstore.repository.category.CategoryRepository;
import org.diatliuk.bookstore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final CategoryDto CATEGORY_DTO = new CategoryDto();
    private static final Category TEST_CATEGORY = new Category();
    private static final CategoryResponseDto RESPONSE_CATEGORY_DTO = new CategoryResponseDto();
    private static final BookDtoWithoutCategoryIds RESPONSE_BOOK = new BookDtoWithoutCategoryIds();
    private static final Book TEST_BOOK = new Book();
    private static final Long INVALID_CATEGORY_ID = -1L;
    private static final Pageable PAGEABLE = Pageable.ofSize(10);
    private static final String FIRST_PART_OF_ERROR_MESSAGE = "Can't find ";
    private static final String SECOND_PART_OF_ERROR_MESSAGE = "by id: ";

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeAll
    static void beforeAll() {
        CATEGORY_DTO.setName("Fiction");
        CATEGORY_DTO.setDescription("Fiction books");

        TEST_CATEGORY.setId(1L);
        TEST_CATEGORY.setName(CATEGORY_DTO.getName());
        TEST_CATEGORY.setDescription(CATEGORY_DTO.getDescription());
        TEST_CATEGORY.setDeleted(false);

        RESPONSE_CATEGORY_DTO.setId(TEST_CATEGORY.getId());
        RESPONSE_CATEGORY_DTO.setName(TEST_CATEGORY.getName());
        RESPONSE_CATEGORY_DTO.setDescription(TEST_CATEGORY.getDescription());

        TEST_BOOK.setId(1L);
        TEST_BOOK.setTitle("Test book 1");
        TEST_BOOK.setAuthor("Tester 1");
        TEST_BOOK.setIsbn("978-0-06-251140-9");
        TEST_BOOK.setPrice(BigDecimal.valueOf(200));
        TEST_BOOK.setDescription("This is a description");
        TEST_BOOK.setCoverImage("link.ua//image1");
        TEST_BOOK.setDeleted(false);
        TEST_BOOK.setCategories(Set.of(TEST_CATEGORY));

        RESPONSE_BOOK.setId(TEST_BOOK.getId());
        RESPONSE_BOOK.setTitle(TEST_BOOK.getTitle());
        RESPONSE_BOOK.setAuthor(TEST_BOOK.getAuthor());
        RESPONSE_BOOK.setIsbn(TEST_BOOK.getIsbn());
        RESPONSE_BOOK.setPrice(TEST_BOOK.getPrice());
        RESPONSE_BOOK.setDescription(TEST_CATEGORY.getDescription());
        RESPONSE_BOOK.setCoverImage(TEST_BOOK.getCoverImage());
    }

    @Test
    @DisplayName("Verify the save() method by using valid data")
    void save_withValidData_returnCategory() {
        when(categoryMapper.toModel(CATEGORY_DTO)).thenReturn(TEST_CATEGORY);
        when(categoryRepository.save(TEST_CATEGORY)).thenReturn(TEST_CATEGORY);
        when(categoryMapper.toResponseDto(TEST_CATEGORY)).thenReturn(RESPONSE_CATEGORY_DTO);

        CategoryResponseDto actualCategory = categoryService.save(CATEGORY_DTO);

        assertEquals(RESPONSE_CATEGORY_DTO, actualCategory);
    }

    @Test
    @DisplayName("Verify the getAll() method")
    void getAll_withPagination_returnCategory() {
        when(categoryRepository.findAll(PAGEABLE))
                                .thenReturn(new PageImpl<>(List.of(TEST_CATEGORY)));
        when(categoryMapper.toResponseDto(any())).thenReturn(RESPONSE_CATEGORY_DTO);

        List<CategoryResponseDto> actualAllCategories = categoryService.getAll(PAGEABLE);

        assertEquals(List.of(RESPONSE_CATEGORY_DTO), actualAllCategories);
    }

    @Test
    @DisplayName("Verify the getById() method by using an existing id")
    void getById_withValidId_returnsCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(TEST_CATEGORY));
        when(categoryMapper.toDto(any())).thenReturn(CATEGORY_DTO);

        CategoryDto actualCategory = categoryService.getById(TEST_CATEGORY.getId());

        assertEquals(CATEGORY_DTO, actualCategory);
    }

    @Test
    @DisplayName("Verify the getById() method by using an existing id")
    void getById_withInvalidId_throwsException() {
        String expectedErrMessage = generateDefaultEntityNotFoundErrorMessage(INVALID_CATEGORY_ID);
        when(categoryRepository.findById(INVALID_CATEGORY_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(INVALID_CATEGORY_ID)
        );

        assertEquals(expectedErrMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Verify the update() method by using an existing id")
    void update_withValidId_returnsCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(TEST_CATEGORY));
        when(categoryMapper.toModel(CATEGORY_DTO)).thenReturn(TEST_CATEGORY);
        when(categoryRepository.save(TEST_CATEGORY)).thenReturn(TEST_CATEGORY);
        when(categoryMapper.toDto(TEST_CATEGORY)).thenReturn(CATEGORY_DTO);

        CategoryDto updatedCategory = categoryService.update(TEST_CATEGORY.getId(), CATEGORY_DTO);

        assertEquals(CATEGORY_DTO, updatedCategory);
    }

    @Test
    @DisplayName("Verify the update() method by using not existing id")
    void update_withInvalidId_returnsCategory() {
        when(categoryRepository.findById(INVALID_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(INVALID_CATEGORY_ID, CATEGORY_DTO)
        );
    }

    @Test
    @DisplayName("Verify the deleteById() method by using an existing id")
    void deleteById_withValidId_void() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(TEST_CATEGORY));

        assertDoesNotThrow(() -> categoryService.deleteById(TEST_CATEGORY.getId()));
    }

    @Test
    @DisplayName("Verify the deleteById() method by using not existing id")
    void deleteById_withInvalidId_throwsException() {
        String expectedErrMessage = generateDefaultEntityNotFoundErrorMessage(INVALID_CATEGORY_ID);
        when(categoryRepository.findById(INVALID_CATEGORY_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteById(INVALID_CATEGORY_ID)
        );

        assertEquals(expectedErrMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Verify the getBooksByCategoryId() using a category id")
    void getBooksByCategoryId_withValidId_returnsBooks() {
        when(bookRepository.findAllByCategoriesId(TEST_CATEGORY.getId()))
                                            .thenReturn(List.of(TEST_BOOK));
        when(bookMapper.toBookDtoWithoutCategoryIds(TEST_BOOK)).thenReturn(RESPONSE_BOOK);

        List<BookDtoWithoutCategoryIds> actualBooks = categoryService
                                                    .getBooksByCategoryId(TEST_CATEGORY.getId());

        assertEquals(List.of(RESPONSE_BOOK), actualBooks);
    }

    private String generateDefaultEntityNotFoundErrorMessage(Long id) {
        return new StringBuilder()
                .append(FIRST_PART_OF_ERROR_MESSAGE)
                .append("a category ")
                .append(SECOND_PART_OF_ERROR_MESSAGE)
                .append(id)
                .toString();
    }
}
