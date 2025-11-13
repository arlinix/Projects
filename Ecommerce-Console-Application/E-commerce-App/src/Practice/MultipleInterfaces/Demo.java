package Practice.MultipleInterfaces;

import java.util.Optional;

public class Demo {
    public static void main(String[] args) {
//        Evening evening;
//        Morning morning;
//        Main main = new Main();
//        main.greetings();

//        System.out.println(evening.greetings());


        Optional<String> name = Optional.ofNullable(null);
        System.out.println(name.isPresent());
    }
}
