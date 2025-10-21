package Books;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    public Book[] filterBooksByAuthor(Author author, Book[] books) {
        List<Book> filtered = new ArrayList<>();

        for(Book book : books){
            for (Author auths : book.getAuthors()){
                if(auths.getIdOfAuthor() == author.getIdOfAuthor()){
                    filtered.add(book);
                    break;
                }
            }
        }
       return filtered.toArray(new Book[0]);
    }

    public Book[] filterBooksByPublisher(Publisher publisher, Book[] books) {
        List<Book> filtered = new ArrayList<>();

        for(Book book : books){
            for(Publisher p : book.getPublisher()){
                if(p.getIdOfPublisher() == publisher.getIdOfPublisher()){
                    filtered.add(book);
                    break;
                }

            }
        }
        return filtered.toArray(new Book[0]);

    }

    // methods keeps books with publishing year inclusively.
    public Book[] filterBooksAfterSpecifiedYear(int yearFromInclusively, Book[] books) {
        List<Book> filtered = new ArrayList<>();

        for(Book b : books){
            if(b.getPublishingYear() >= yearFromInclusively){
                filtered.add(b);
            }
        }
        return filtered.toArray(new Book[0]);

    }

}
