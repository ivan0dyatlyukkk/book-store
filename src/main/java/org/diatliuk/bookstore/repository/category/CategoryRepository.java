package org.diatliuk.bookstore.repository.category;

import org.diatliuk.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
