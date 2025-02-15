package be.lycoops.vincent.iv.model.json;

public class SpeedRange {

    private int pos;

    private String description;

    private int level;

    private int expectedStat;

    public SpeedRange(String description, int level, int expectedStat, int pos) {
        this.description = description;
        this.level = level;
        this.expectedStat = expectedStat;
        this.pos = pos;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public int getExpectedStat() {
        return expectedStat;
    }

    public int getPos() {
        return pos;
    }
}
