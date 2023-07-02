package com.spring.ivannikov.project2.controllers;

import com.spring.ivannikov.project2.entity.Book;
import com.spring.ivannikov.project2.entity.Person;
import com.spring.ivannikov.project2.service.PeopleServiceImpl;
import com.spring.ivannikov.project2.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleServiceImpl personService;
    private final PersonValidator personValidator;

    public PeopleController(PeopleServiceImpl personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String findAllPeople(Model model) {
        List<Person> people = personService.findAll();
        model.addAttribute("people", people);
        return "people/all-people";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new-person";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") Long id, Model model) {
        Person person = personService.findPerson(id);
        List<Book> books = personService.findAllBookForPerson(id);
        model.addAttribute("person", person);
        model.addAttribute("books", books);
        return "people/page-person";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") Long id, Model model) {
        Person person = personService.findPerson(id);
        model.addAttribute("person", person);
        return "people/edit-person";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()) {
            return "people/new-person";
        }
        personService.save(person);
        return "redirect:/people";
    }

    @PatchMapping("/{id}/edit")
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                               @PathVariable Long id) {
        if (bindingResult.hasErrors()){
            return "people/edit-person";
        }
        personService.updatePerson(person, id);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id);
        return "redirect:/people";
    }

}
