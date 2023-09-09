package org.diatliuk.bookstore.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.exception.SpecificationProviderNotFoundException;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.repository.specification.SpecificationProvider;
import org.diatliuk.bookstore.repository.specification.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(specificationProviders -> specificationProviders.getKey().equals(key))
                .findAny()
                .orElseThrow(() -> new SpecificationProviderNotFoundException("Can't "
                        + "find an appropriate specification provider for the key: " + key));
    }
}
