package com.spring.ivannikov.project2.util;

import com.spring.ivannikov.project2.entity.Person;
import com.spring.ivannikov.project2.service.PeopleServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleServiceImpl peopleService;

    public PersonValidator(PeopleServiceImpl peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.getPersonByFullName(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "", "Человек с таким ФИО уже существует");
        }
    }
}
