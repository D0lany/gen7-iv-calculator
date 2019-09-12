package be.lycoops.vincent.iv.model;

public class Damage {

    private int minRoll;

    private int maxRoll;

    private int minRollCrit;

    private int maxRollCrit;

    public Damage(int minRoll, int maxRoll, int minRollCrit, int maxRollCrit) {
        this.minRoll = minRoll;
        this.maxRoll = maxRoll;
        this.minRollCrit = minRollCrit;
        this.maxRollCrit = maxRollCrit;
    }

    public int getMinRoll() {
        return minRoll;
    }

    public int getMaxRoll() {
        return maxRoll;
    }

    public int getMinRollCrit() {
        return minRollCrit;
    }

    public int getMaxRollCrit() {
        return maxRollCrit;
    }
}
