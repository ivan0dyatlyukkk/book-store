package org.diatliuk.bookstore;

import java.math.BigDecimal;
import java.util.List;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book kobzar = new Book("Kobzar",
                    "Shevchenko T.G.",
                    "123",
                    BigDecimal.valueOf(200),
                    null,
                    null);
            Book goldEditionKobzar = new Book("Kobzar: Gold Edition",
                    "Shevchenko T.G.",
                    "1235",
                    BigDecimal.valueOf(1000),
                    "The second part of the most famous book",
                    null);
            bookService.save(kobzar);
            bookService.save(goldEditionKobzar);
            List<Book> allBooks = bookService.findAll();
            System.out.println(allBooks);
        };
    }
}
