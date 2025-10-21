package SweetStore;

public class DemoSweetStore {
    public static void main(String[] args) {
        Present present = new Present();
        present.addSweet(new Candy("ThreeMusketeers", 0.03, 0.02));
        present.addSweet(new Lollipop("Chupa Chups", 0.05, 0.03));
        present.addSweet(new Cookie("Oreo", 0.03, 0.01));

        System.out.println("Total Present weight: " + present.calculateTotalWeight());

        System.out.println("All Sweets in Present");
        for(Sweets s : present.getSweets()){
            System.out.println(s.getSweetName());
        }

        System.out.println("Sweets by Sugar Level");
        for (Sweets s : present.filterSweetsBySugarRange(0, 0.02)){
            System.out.println(s);
        }
    }
}
