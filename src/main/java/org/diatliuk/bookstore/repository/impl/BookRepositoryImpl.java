package org.diatliuk.bookstore.repository.impl;

import java.util.List;
import java.util.Optional;
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
    private static final String ENTITY_ID_PARAM = "id";
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
    public Optional<Book> findBookById(Long id) {
        String hql = "FROM Book WHERE id = :id";
        try (Session session = sessionFactory.openSession()) {
            Query<Book> getBookById = session.createQuery(hql, Book.class);
            getBookById.setParameter(ENTITY_ID_PARAM, id);
            return Optional.ofNullable(getBookById.getSingleResult());
        } catch (Exception e) {
            throw new DataProcessingException("Can't find a book by id: " + id, e);
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
