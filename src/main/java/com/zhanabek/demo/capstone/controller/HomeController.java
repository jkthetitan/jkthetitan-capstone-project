package com.zhanabek.demo.capstone.controller;

import com.zhanabek.demo.capstone.component.Book;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    static String description = "The ultimate science fiction classic: for more than one hundred years, this compelling tale of the Martian invasion of Earth has enthralled readers with a combination of imagination and incisive commentary on the imbalance of power that continues to be relevant today. The style is revolutionary for its era, employing a sophisticated first and third person account of the events which is both personal and focused on the holistic downfall of Earth's society. The Martians, as evil, mechanical and unknown a threat they are, remain daunting in today's society, where, despite technology's mammoth advances, humanity's hegemony over Earth is yet to be called into question. In Well's introduction to the book, where the character discusses with the later deceased Ogilvy about astronomy and the possibility of alien life defeating the 'savage' (to them) nineteenth-century Britain, is he insinuating that this is the truth and fate of humanity? It's up to you to decide…";
    static List<Book> books;
    static {
        books = List.of(
                new Book(
                        "Clean Code",
                        "Robert C. Martin",
                        "9780132350884",
                        "Prentice Hall",
                        2008,
                        5,
                        description,
                        "English",
                        464,
                        "RETURNED",
                        "/images/books/book_cover.jpg"
                ),

                new Book(
                        "Effective Java",
                        "Joshua Bloch",
                        "9780134685991",
                        "Addison-Wesley",
                        2018,
                        3,
                        description,
                        "English",
                        416,
                        "PENDING", "/images/books/book_cover.jpg"
                ),

                new Book(
                        "Spring in Action",
                        "Craig Walls",
                        "9781617294945",
                        "Manning",
                        2022,
                        4,
                        description,
                        "English",
                        520,
                        "OVERDUE", "/images/books/book_cover.jpg"
                ),

                new Book(
                        "Design Patterns",
                        "Erich Gamma",
                        "9780201633610",
                        "Addison-Wesley",
                        1994,
                        2,
                        description,
                        "English",
                        395,
                        "RETURNED", "/images/books/book_cover.jpg"
                ),

                new Book(
                        "Refactoring",
                        "Martin Fowler",
                        "9780201485677",
                        "Addison-Wesley",
                        2018,
                        6,
                        description,
                        "English",
                        448,
                        "OVERDUE", "/images/books/book_cover.jpg"
                ),

                new Book(
                        "The Pragmatic Programmer",
                        "Andrew Hunt",
                        "9780201616224",
                        "Addison-Wesley",
                        1999,
                        7,
                        description,
                        "English",
                        352,
                        "PENDING", "/images/books/book_cover.jpg"
                ),

                new Book(
                        "You Don’t Know JS",
                        "Kyle Simpson",
                        "9781491904244",
                        "O’Reilly Media",
                        2015,
                        5,
                        description,
                        "English",
                        278,
                        "RETURNED", "/images/books/book_cover.jpg"
                ),

                new Book(
                        "Head First Design Patterns",
                        "Eric Freeman",
                        "9780596007126",
                        "O’Reilly Media",
                        2004,
                        4,
                        "Easy introduction to design patterns.",
                        "English",
                        694,
                        "RETURNED", "/images/books/book_cover.jpg"
                ),

                new Book(
                        "Clean Architecture",
                        "Robert C. Martin",
                        "9780134494166",
                        "Prentice Hall",
                        2017,
                        3,
                        "Architecture principles for software systems.",
                        "English",
                        432,
                        "PENDING", "/images/books/book_cover.jpg"
                ),

                new Book(
                        "Domain-Driven Design",
                        "Eric Evans",
                        "9780321125217",
                        "Addison-Wesley",
                        2003,
                        2,
                        "Modeling complex software domains.",
                        "English",
                        560,
                        "RETURNED", "/images/books/book_cover.jpg"
                )
        );
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "index";
    }
    @GetMapping("/books")
    public String books(Model model) {
        model.addAttribute("content", "fragments/book-search :: content");
        return "index";
    }
    @GetMapping("/books/search")
    public String bookSearch(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query == null) {
            model.addAttribute("content", "fragments/book-search :: content");
        } else {
            model.addAttribute("books", books);
            model.addAttribute("content", "fragments/book-search-result :: content");
        }
        return "index";
    }
    @GetMapping("/books/view/1")
    public String view(Model model) {
        model.addAttribute("content", "fragments/book-info :: content");
        model.addAttribute("book", books.get(0));
        return "index";
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("content", "fragments/login :: content");
        return "index";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("content", "fragments/register :: content");
        return "index";
    }
    @GetMapping("/loans/all")
    public String getAllBooks(Model model) {
        model.addAttribute("books", books);
        model.addAttribute("type", "ALL");
        model.addAttribute("title", "All My Books");
        model.addAttribute("content", "fragments/request :: content");
        model.addAttribute("leftSidebar", "fragments/sidebar :: sidebar");
        return "index";
    }
    @GetMapping("/loans/overdue")
    public String getOverdueBooks(Model model) {
        List<Book> overdueBooks = new ArrayList<>();
        for (Book book : books) {
            if ("OVERDUE".equals(book.getStatus())) {
                overdueBooks.add(book);
            }
        }
        model.addAttribute("books", overdueBooks);
        model.addAttribute("type", "OVERDUE");
        model.addAttribute("title", "Overdue Books");
        model.addAttribute("content", "fragments/request :: content");
        model.addAttribute("leftSidebar", "fragments/sidebar :: sidebar");
        return "index";
    }
    @GetMapping("/loans/active")
    public String getActiveBooks(Model model) {
        List<Book> activeBooks = new ArrayList<>();
        for (Book book : books) {
            if ("ACTIVE".equals(book.getStatus())) {
                activeBooks.add(book);
            }
        }
        model.addAttribute("books", activeBooks);
        model.addAttribute("type", "ACTIVE");
        model.addAttribute("title", "Active Books");
        model.addAttribute("content", "fragments/request :: content");
        model.addAttribute("leftSidebar", "fragments/sidebar :: sidebar");
        return "index";
    }
    @GetMapping("/loans/pending")
    public String getPendingBooks(Model model) {
        List<Book> pendingBooks = new ArrayList<>();
        for (Book book : books) {
            if ("PENDING".equals(book.getStatus())) {
                pendingBooks.add(book);
            }
        }
        model.addAttribute("books", pendingBooks);
        model.addAttribute("type", "PENDING");
        model.addAttribute("title", "Pending Books");
        model.addAttribute("content", "fragments/request :: content");
        model.addAttribute("leftSidebar", "fragments/sidebar :: sidebar");
        return "index";
    }
    @GetMapping("/loans/returned")
    public String getReturnedBooks(Model model) {
        List<Book> returnedBooks = new ArrayList<>();
        for (Book book : books) {
            if ("RETURNED".equals(book.getStatus())) {
                returnedBooks.add(book);
            }
        }
        model.addAttribute("books", returnedBooks);
        model.addAttribute("type", "RETURNED");
        model.addAttribute("title", "Returned Books");
        model.addAttribute("content", "fragments/request :: content");
        model.addAttribute("leftSidebar", "fragments/sidebar :: sidebar");
        return "index";
    }

}
