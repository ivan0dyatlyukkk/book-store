package org.diatliuk.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.book.BookSearchParametersDto;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.repository.specification.SpecificationBuilder;
import org.diatliuk.bookstore.repository.specification.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE_PARAM_KEY = "title";
    private static final String AUTHOR_PARAM_KEY = "author";
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (isParameterPresent(bookSearchParametersDto.getTitle())) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(TITLE_PARAM_KEY)
                    .getSpecification(bookSearchParametersDto.getTitle()));
        }
        if (isParameterPresent(bookSearchParametersDto.getAuthor())) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_PARAM_KEY)
                    .getSpecification(bookSearchParametersDto.getAuthor()));
        }
        return specification;
    }

    private boolean isParameterPresent(String[] params) {
        return params != null && params.length > 0;
    }
}
