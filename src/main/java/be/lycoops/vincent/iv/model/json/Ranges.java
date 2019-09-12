package be.lycoops.vincent.iv.model.json;

import java.util.List;

public class Ranges {

    private double crit;

    private List<Range> offensiveRanges;

    private List<Range> defensiveRanges;

    private List<SpeedRange> speedRanges;

    public Ranges(double crit, List<Range> offensiveRanges, List<Range> defensiveRanges, List<SpeedRange> speedRanges) {
        this.crit = crit;
        this.offensiveRanges = offensiveRanges;
        this.defensiveRanges = defensiveRanges;
        this.speedRanges = speedRanges;
    }

    public double getCrit() {
        return crit;
    }

    public List<Range> getOffensiveRanges() {
        return offensiveRanges;
    }

    public List<Range> getDefensiveRanges() {
        return defensiveRanges;
    }

    public List<SpeedRange> getSpeedRanges() {
        return speedRanges;
    }
}
