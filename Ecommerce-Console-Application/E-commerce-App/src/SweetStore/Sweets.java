package SweetStore;

public abstract class Sweets {
    private String sweetName;
    private double sweetWeight;
    private double sugarWeight;

    public Sweets(String sweetName, double sweetWeight, double sugarWeight) {
        this.sweetName = sweetName;
        this.sweetWeight = sweetWeight;
        this.sugarWeight = sugarWeight;
    }

    @Override
    public String toString() {
        return "Sweets{" +
                "sweetName='" + sweetName + '\'' +
                ", sweetWeight=" + sweetWeight +
                ", sugarWeight=" + sugarWeight +
                '}';
    }

    public String getSweetName() {
        return sweetName;
    }

    public double getSweetWeight() {
        return sweetWeight;
    }

    public double getSugarWeight() {
        return sugarWeight;
    }
}
