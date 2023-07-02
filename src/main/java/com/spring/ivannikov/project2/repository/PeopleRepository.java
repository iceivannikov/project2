package com.spring.ivannikov.project2.repository;

import com.spring.ivannikov.project2.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {
    Optional<Person> findAllByFullName(String fullName);
}
