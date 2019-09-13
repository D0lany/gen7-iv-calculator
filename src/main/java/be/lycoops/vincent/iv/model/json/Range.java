package be.lycoops.vincent.iv.model.json;

public class Range {

    private int pos;

    private String description;

    private int attackerLevel;

    private int defenderLevel;

    private String damageType;

    private int movePower;

    private int stat;

    private double modifier;

    private double attackModifier;

    private double defenseModifier;

    private int hp;

    public Range(String description, int attackerLevel, int defenderLevel, String damageType, int movePower, int stat, double modifier, double attackModifier, double defenseModifier, int hp, int pos) {
        this.description = description;
        this.attackerLevel = attackerLevel;
        this.defenderLevel = defenderLevel;
        this.damageType = damageType;
        this.movePower = movePower;
        this.stat = stat;
        this.modifier = modifier;
        this.attackModifier = attackModifier;
        this.defenseModifier = defenseModifier;
        this.hp = hp;
        this.pos = pos;
    }

    public String getDescription() {
        return description;
    }

    public int getAttackerLevel() {
        return attackerLevel;
    }

    public int getDefenderLevel() {
        return defenderLevel;
    }

    public String getDamageType() {
        return damageType;
    }

    public int getMovePower() {
        return movePower;
    }

    public int getStat() {
        return stat;
    }

    public double getModifier() {
        return modifier;
    }

    public double getAttackModifier() {
        return attackModifier;
    }

    public double getDefenseModifier() {
        return defenseModifier;
    }

    public int getHp() {
        return hp;
    }

    public int getPos() {
        return pos;
    }
}
