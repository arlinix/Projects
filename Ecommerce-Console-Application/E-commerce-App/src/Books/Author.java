package Books;

public class Author {
    private int idOfAuthor;
    private String firstNameAuthor;
    private String lastNameAuthor;

    @Override
    public String toString() {
        return "Author{" +
                "idOfAuthor=" + idOfAuthor +
                ", firstNameAuthor='" + firstNameAuthor + '\'' +
                ", lastNameAuthor='" + lastNameAuthor + '\'' +
                '}';
    }

    public Author(int idOfAuthor, String firstNameAuthor, String lastNameAuthor) {
        this.idOfAuthor = idOfAuthor;
        this.firstNameAuthor = firstNameAuthor;
        this.lastNameAuthor = lastNameAuthor;
    }

    public int getIdOfAuthor() {
        return idOfAuthor;
    }

    public void setIdOfAuthor(int idOfAuthor) {
        this.idOfAuthor = idOfAuthor;
    }

    public String getFirstNameAuthor() {
        return firstNameAuthor;
    }

    public void setFirstNameAuthor(String firstNameAuthor) {
        this.firstNameAuthor = firstNameAuthor;
    }

    public String getLastNameAuthor() {
        return lastNameAuthor;
    }

    public void setLastNameAuthor(String lastNameAuthor) {
        this.lastNameAuthor = lastNameAuthor;
    }
}
