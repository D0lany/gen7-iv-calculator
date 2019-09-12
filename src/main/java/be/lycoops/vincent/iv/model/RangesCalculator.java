package be.lycoops.vincent.iv.model;

import be.lycoops.vincent.iv.model.json.Range;
import be.lycoops.vincent.iv.model.json.Ranges;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

import static java.util.Objects.isNull;

public class RangesCalculator {

    private Ranges ranges = new Ranges(
            1.5,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
    );

    @Inject
    private Pokemon pokemon;

    public void loadRanges() {
        System.out.println("loading pokemon from file");
        String rangesFile = String.format("be/lycoops/vincent/iv/ranges/%s.json", EffortValueProvider.getRoute());
        String json = null;

        try {
            Path path = FilePathProvider.getPath(rangesFile);
            if (path != null) {
                json = Files.readString(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (json == null) {
            System.out.println("damage ranges could not be loaded");
        } else {
            Gson g = new Gson();

            this.ranges = g.fromJson(json, Ranges.class);
        }
    }

//    TODO add defensive and speed ranges and properly do this shit (its ugly rn)
    public String calculateAll() {
        String ret = "";
        if (!isNull(pokemon.getNature())) {
            for (Range range : ranges.getOffensiveRanges()) {
                Map<Stat, Integer> effortValues = EffortValueProvider.getEffortValues(range.getAttackerLevel());
                Stat stat = range.getDamageType().equals("physical") ? Stat.ATK : range.getDamageType().equals("special") ? Stat.SP_ATK : null;
                int baseStat = range.getDamageType().equals("physical") ?
                        pokemon.pokemonModelFromFileProperty().get().getBaseStats().getAtk() : range.getDamageType().equals("special") ?
                        pokemon.pokemonModelFromFileProperty().get().getBaseStats().getSpAtk() : 0;
                if (!isNull(stat) && baseStat > 0) {
//                    TODO get actual IVs (min and max?)
                    int effectiveStat = (int) Math.floor(Math.floor((2 * baseStat + 31 + effortValues.get(stat)) * range.getAttackerLevel() / 100.0 + 5) * (pokemon.getNature().getStat(stat) == 1 ? 1.1 : pokemon.getNature().getStat(stat) == -1 ? 0.9 : 1));
                    Damage calculate = calculate(range.getAttackerLevel(),
                            range.getMovePower(),
                            (int) Math.floor(effectiveStat * range.getAttackModifier()),
                            (int) Math.floor(range.getStat() * range.getDefenseModifier()),
                            range.getModifier(),
                            ranges.getCrit());
                    ret += "" + range.getDescription() + "\n\r" + " min: " + calculate.getMinRoll()
                            + " max: " + calculate.getMaxRoll()
                            + " min(CRIT): " + calculate.getMinRollCrit()
                            + " max(CRIT): " + calculate.getMaxRollCrit()
                            + " enemy HP: " + range.getHp() + "\n\r\n" +
                            "\n";
                }
            }
        }
        return ret;
    }

    private Damage calculate(int level, int power, int attack, int defense, double modifier, double crit) {
        double baseCalc = (Math.floor((Math.floor((2 * level) / 5.0 + 2) * power * attack) / defense) / 50 + 2) * modifier;
        return new Damage(
                (int) Math.floor(baseCalc * 0.85),
                (int) Math.floor(baseCalc),
                (int) Math.floor(baseCalc * 0.85 * crit),
                (int) Math.floor(baseCalc * crit)
        );
    }
}
