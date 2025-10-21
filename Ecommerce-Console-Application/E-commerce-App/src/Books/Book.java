package Books;

import java.math.BigDecimal;
import java.util.Arrays;

public class Book {
    private int idOfBook;
    private String nameOfBook;
    private Author[] authors;
    private Publisher[] publisher;
    private int publishingYear;
    private int amountOfPages;
    private BigDecimal priceOfBook;
    private CoverType coverType;

    public Book(int idOfBook,
                String nameOfBook,
                Author[] authors,
                Publisher[] publisher,
                int publishingYear,
                int amountOfPages,
                BigDecimal priceOfBook,
                CoverType coverType) {
        this.idOfBook = idOfBook;
        this.nameOfBook = nameOfBook;
        this.authors = authors;
        this.publisher = publisher;
        this.publishingYear = publishingYear;
        this.amountOfPages = amountOfPages;
        this.priceOfBook = priceOfBook;
        this.coverType = coverType;
    }

    @Override
    public String toString() {
        return "Book{" +
                "idOfBook=" + idOfBook +
                ", nameOfBook='" + nameOfBook + '\'' +
                ", authors=" + Arrays.toString(authors) +
                ", publisher=" + Arrays.toString(publisher) +
                ", publishingYear=" + publishingYear +
                ", amountOfPages=" + amountOfPages +
                ", priceOfBook=" + priceOfBook +
                ", coverType=" + coverType +
                '}';
    }

    public int getIdOfBook() {
        return idOfBook;
    }

    public void setIdOfBook(int idOfBook) {
        this.idOfBook = idOfBook;
    }

    public String getNameOfBook() {
        return nameOfBook;
    }

    public void setNameOfBook(String nameOfBook) {
        this.nameOfBook = nameOfBook;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    public Publisher[] getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher[] publisher) {
        this.publisher = publisher;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(int publishingYear) {
        this.publishingYear = publishingYear;
    }

    public int getAmountOfPages() {
        return amountOfPages;
    }

    public void setAmountOfPages(int amountOfPages) {
        this.amountOfPages = amountOfPages;
    }

    public BigDecimal getPriceOfBook() {
        return priceOfBook;
    }

    public void setPriceOfBook(BigDecimal priceOfBook) {
        this.priceOfBook = priceOfBook;
    }

    public CoverType getCoverType() {
        return coverType;
    }

    public void setCoverType(CoverType coverType) {
        this.coverType = coverType;
    }
}
