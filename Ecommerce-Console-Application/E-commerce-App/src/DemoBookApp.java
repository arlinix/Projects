import org.w3c.dom.ls.LSOutput;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DemoBookApp {
    public static void main(String[] args) {
        Map<Integer, Author> authors = new HashMap<>();
        authors.put(1, (new Author(1, "Jon", "Johnson")));
        authors.put(2, (new Author(2, "William", "Wilson")));
        authors.put(3,( new Author(3, "Walter", "Peterson")));
        authors.put(4, (new Author(4, "Craig", "Gregory")));

        Map<Integer, Publisher> publishers = new HashMap<>();
        publishers.put(1, (new Publisher(1, "Publisher_1")));
        publishers.put(2, (new Publisher(2, "Publisher_2")));
        publishers.put(3, (new Publisher(3, "Publisher_3")));


        Book[] books = new Book[] {
                new Book(1, "Book_1",
                        new Author[] { authors.get(1) },
                        new Publisher[]{publishers.get(1)},
                        1990, 231, BigDecimal.valueOf(24.99), CoverType.PAPERBACK),

                new Book(2, "Book_2",
                        new Author[] {authors.get(1),
                                       authors.get(2) },
                        new Publisher[] {publishers.get(2)},
                        2000, 120, BigDecimal.valueOf(14.99), CoverType.PAPERBACK),

                new Book(3, "Book_3",
                        new Author[] { authors.get(3) },
                        new Publisher []{publishers.get(1)},
                        1997, 350, BigDecimal.valueOf(34.99), CoverType.HARDCOVER),


                new Book(4, "Book_4",
                        new Author[] { authors.get(4)},
                        new Publisher []{publishers.get(3)},
                        1992, 185, BigDecimal.valueOf(19.99), CoverType.PAPERBACK) };

        BookService service = new BookService();
//        System.out.println("Books by Jon Johnson:");
//        for (Book b : service.filterBooksByAuthor(authors.get(1), books)) {
//            System.out.println(b);
//        }

//        System.out.println("Books By Publisher_2");
//        for (Book b : service.filterBooksByPublisher(publishers.get(2),books)){
//            System.out.println(b);
//        }

        System.out.println("Books From Year");
        for (Book b : service.filterBooksAfterSpecifiedYear(1992, books)){
            System.out.println(b.getNameOfBook());
        }

//        System.out.println(Arrays.toString(service.filterBooksByAuthor(authors.get(1), books)));




    }






}
