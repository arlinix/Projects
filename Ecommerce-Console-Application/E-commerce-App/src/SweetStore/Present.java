package SweetStore;

import java.util.ArrayList;
import java.util.List;

public class Present {
   private List<Sweets> sweets = new ArrayList<>();

        // the method filters sweets by sugar weight inclusively
    public Sweets[] filterSweetsBySugarRange(double minSugarWeight, double maxSugarWeight) {
        List<Sweets> filtered = new ArrayList<>();
        for(Sweets sweet : sweets){
            if(sweet.getSugarWeight() >= minSugarWeight && sweet.getSugarWeight() <= maxSugarWeight){
                filtered.add(sweet);
            }
        }
        return filtered.toArray(new Sweets[0]);
    }

    // the method calculates total weight of the present
    public double calculateTotalWeight() {
        double weight = 0;
        for (Sweets sweet : sweets){
            weight += sweet.getSweetWeight();
        }

        return weight;
    }

    // the method that adds sweet to the present
    public void addSweet(Sweets sweet) {
        sweets.add(sweet);
    }

    // the method returns copy of the Sweet[] array so that nobody could
// modify state of the present without addSweet() method.
// Also array shouldn’t contain null values.
    public Sweets[] getSweets() {
        return sweets.toArray(new Sweets[0]);
    }

}
