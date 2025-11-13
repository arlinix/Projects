package Practice.MultipleInterfaces;

public interface Evening {
    default void greetings() {
        System.out.println("Good Evening");
    }
//    void greetings();

}
