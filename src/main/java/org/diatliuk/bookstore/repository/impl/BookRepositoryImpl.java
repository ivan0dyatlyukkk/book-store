package org.diatliuk.bookstore.repository.impl;

import java.util.List;
import org.diatliuk.bookstore.exception.DataProcessingException;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert the book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        String hql = "FROM Book";
        try (Session session = sessionFactory.openSession()) {
            Query<Book> getAllBooks = session.createQuery(hql, Book.class);
            return getAllBooks.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get any books in the DB!", e);
        }
    }
}
