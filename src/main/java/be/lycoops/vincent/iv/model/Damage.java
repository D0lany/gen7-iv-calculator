package be.lycoops.vincent.iv.model;

import be.lycoops.vincent.iv.model.json.Range;

public class Damage {

    private Range range;

    private int iv;

    private Stat stat;

    private int minRoll;

    private int maxRoll;

    private int minRollCrit;

    private int maxRollCrit;

    public Damage(Range range, int iv, Stat stat, int minRoll, int maxRoll, int minRollCrit, int maxRollCrit) {
        this.range = range;
        this.iv = iv;
        this.stat = stat;
        this.minRoll = minRoll;
        this.maxRoll = maxRoll;
        this.minRollCrit = minRollCrit;
        this.maxRollCrit = maxRollCrit;
    }

    public Range getRange() {
        return range;
    }

    public int getIv() {
        return iv;
    }

    public Stat getStat() {
        return stat;
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

    @Override
    public String toString() {
        return range.getDescription() + "(" + stat.toString().toUpperCase() + " IV: " + iv + ")" + "\n\r\n\r"
                + "min: " + this.getMinRoll() + "\n\r"
                + "max: " + this.getMaxRoll() + "\n\r"
                + "min(CRIT): " + this.getMinRollCrit() + "\n\r"
                + "max(CRIT): " + this.getMaxRollCrit() + "\n\r"
                + "enemy HP: " + this.range.getHp() + "\n\r\n\r\n\r";
    }
}
