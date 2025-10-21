package Books;

public class Publisher {
    private int idOfPublisher;
    private String publisherName;

    @Override
    public String toString() {
        return "Publisher{" +
                "idOfPublisher=" + idOfPublisher +
                ", publisherName='" + publisherName + '\'' +
                '}';
    }

    public Publisher(int idOfPublisher, String publisherName) {
        this.idOfPublisher = idOfPublisher;
        this.publisherName = publisherName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public int getIdOfPublisher() {
        return idOfPublisher;
    }

    public void setIdOfPublisher(int idOfPublisher) {
        this.idOfPublisher = idOfPublisher;
    }
}
