package com.spring.ivannikov.project2.service;

import com.spring.ivannikov.project2.entity.Book;
import com.spring.ivannikov.project2.entity.Person;
import com.spring.ivannikov.project2.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksServiceImpl {
    public static final String YEAR_OF_PUBLISHING = "yearOfPublishing";
    private final BooksRepository booksRepository;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(Integer page, Integer booksPerPage, Boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by(YEAR_OF_PUBLISHING)))
                    .getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Integer getAllCount() {
        List<Book> books = booksRepository.findAll();
        return books.size();
    }

    public Person findPersonForBook(Long id) {
        return booksRepository
                .findById(id)
                .map(Book::getPerson)
                .orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    public Book showBook(Long id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateBook(Book book, Long id) {
        Book bookToBeUpdated = null;
        Optional<Book> optionalBook = booksRepository.findById(id);
        if (optionalBook.isPresent()) {
            bookToBeUpdated = optionalBook.get();
        }
        book.setId(id);
        assert bookToBeUpdated != null;
        book.setPerson(bookToBeUpdated.getPerson());
        booksRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(Long id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setPerson(null);
                    book.setTakeAt(null);
                });
    }

    @Transactional
    public void assign(Long id, Person person) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setPerson(person);
                    book.setTakeAt(new Date());
                }
        );
    }

    public List<Book> searchByName(String name) {
        return booksRepository.findByNameStartingWith(name);
    }
}
