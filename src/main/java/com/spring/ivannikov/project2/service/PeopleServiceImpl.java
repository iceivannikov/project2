package com.spring.ivannikov.project2.service;

import com.spring.ivannikov.project2.entity.Book;
import com.spring.ivannikov.project2.entity.Person;
import com.spring.ivannikov.project2.repository.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleServiceImpl {

    public static final int TEN_DAYS = 864000000;
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleServiceImpl(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public List<Book> findAllBookForPerson(Long id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakeAt().getTime() - new Date().getTime());
                book.setExpired(diffInMillies > TEN_DAYS);
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(Person person, Long id) {
        person.setId(id);
        peopleRepository.save(person);
    }

    public Person findPerson(Long id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deletePerson(Long id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findAllByFullName(fullName);
    }
}
