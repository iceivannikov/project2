package com.spring.ivannikov.project2.controllers;

import com.spring.ivannikov.project2.entity.Book;
import com.spring.ivannikov.project2.entity.Person;
import com.spring.ivannikov.project2.service.BooksServiceImpl;
import com.spring.ivannikov.project2.service.PeopleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/books")
public class BooksController {

    public static final int ONE = 1;
    public static final int START_PAGE = 1;
    private final BooksServiceImpl bookService;
    private final PeopleServiceImpl personService;

    public BooksController(BooksServiceImpl bookService, PeopleServiceImpl personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String findAllBook(Model model,
                              @RequestParam(name = "page", defaultValue = "1") Integer page,
                              @RequestParam(name = "books_per_page", defaultValue = "10") Integer booksPerPage,
                              @RequestParam(name = "sort_by_year", defaultValue = "true") Boolean sortByYear) {
        List<Book> books = bookService.findAll(page, booksPerPage, sortByYear);
        model.addAttribute("books", books);
        model.addAttribute("page", page);
        int totalPages = (int) Math.ceil(1.0 * bookService.getAllCount() / booksPerPage) - ONE;
        if (totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(START_PAGE, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }
        return "books/all-books";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new-book";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new-book";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") Long id, Model model, @ModelAttribute("person") Person person) {
        Book book = bookService.showBook(id);
        List<Person> people = personService.findAll();
        Person owner = bookService.findPersonForBook(id);
        model.addAttribute("book", book);
        if (Objects.isNull(owner)) {
            model.addAttribute("people", people);
        } else {
            model.addAttribute("owner", owner);
        }
        return "books/page-book";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.showBook(id);
        model.addAttribute("book", book);
        return "books/edit-book";
    }

    @PatchMapping("/{id}/edit")
    public String editBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                           @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "books/edit-book";
        }
        bookService.updateBook(book, id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") Long id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") Long id, @ModelAttribute("person") Person person) {
        bookService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchBookPage() {
        return "books/search-book";
    }
    @PostMapping("/search")
    public String searchBookResult(Model model, @RequestParam("name") String name){
        List<Book> books = bookService.searchByName(name);
        model.addAttribute("books", books);
        return "books/search-book";
    }
}
