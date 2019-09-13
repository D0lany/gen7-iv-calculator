package be.lycoops.vincent.iv.model;

import be.lycoops.vincent.iv.model.json.Range;
import be.lycoops.vincent.iv.model.json.Ranges;
import be.lycoops.vincent.iv.model.json.SpeedRange;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        System.out.println("loading ranges from file");

        String rangesDir = AdditionalFilesProvider.getRangesDir();
        String rangesFile = "";
        if (!rangesDir.isEmpty()) {
            rangesFile = String.format("%s/%s.json", rangesDir, EffortValueProvider.getRoute());
        } else {
            rangesFile = String.format("be/lycoops/vincent/iv/ranges/%s.json", EffortValueProvider.getRoute());
        }
        Path path = FilePathProvider.getPath(rangesFile);
        if (path == null) {

            rangesFile = String.format("be/lycoops/vincent/iv/ranges/%s.json", EffortValueProvider.getRoute());
            path = FilePathProvider.getPath(rangesFile);

            if (path == null) {
                System.out.println("ranges file could not be loaded for route: " + EffortValueProvider.getRoute());
            }
        }

        String json = null;

        try {
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
    public List<Damage> calculateAll() {
        List<Damage> damageRanges = new ArrayList<>();
        if (!isNull(pokemon.getNature())) {
            for (Range range : ranges.getOffensiveRanges()) {
                Stat stat = range.getDamageType().equals("physical") ? Stat.ATK : range.getDamageType().equals("special") ? Stat.SP_ATK : null;
                int baseStat = range.getDamageType().equals("physical") ?
                        pokemon.pokemonModelFromFileProperty().get().getBaseStats().getAtk() : range.getDamageType().equals("special") ?
                        pokemon.pokemonModelFromFileProperty().get().getBaseStats().getSpAtk() : 0;

                if (!isNull(stat) && baseStat > 0) {
                    int effortValue = EffortValueProvider.getEffortValues(range.getAttackerLevel()).get(stat) + pokemon.getAdditionalEffortValues().getEffortValue(stat).get();

                    for (int iv : List.of(pokemon.getMinIndividualValues().get(stat).get(),pokemon.getMaxIndividualValues().get(stat).get())) {
                        int effectiveStat = getExpectedStat(range.getAttackerLevel(),
                                iv,
                                effortValue,
                                baseStat,
                                pokemon.getNature().getStat(stat));

                        damageRanges.add(calculate(range.getAttackerLevel(),
                                (int) Math.floor(effectiveStat * range.getAttackModifier()),
                                (int) Math.floor(range.getStat() * range.getDefenseModifier()),
                                ranges.getCrit(),
                                range,
                                stat,
                                iv));
                    }
                }
            }

            for (Range range : ranges.getDefensiveRanges()) {
                Stat stat = range.getDamageType().equals("physical") ? Stat.DEF : range.getDamageType().equals("special") ? Stat.SP_DEF : null;
                int baseStat = range.getDamageType().equals("physical") ?
                        pokemon.pokemonModelFromFileProperty().get().getBaseStats().getDef() : range.getDamageType().equals("special") ?
                        pokemon.pokemonModelFromFileProperty().get().getBaseStats().getSpDef() : 0;

                if (!isNull(stat) && baseStat > 0) {
                    int effortValue = EffortValueProvider.getEffortValues(range.getDefenderLevel()).get(stat) + pokemon.getAdditionalEffortValues().getEffortValue(stat).get();

                    for (int iv : List.of(pokemon.getMinIndividualValues().get(stat).get(),pokemon.getMaxIndividualValues().get(stat).get())) {
                        int effectiveStat = getExpectedStat(range.getDefenderLevel(),
                                iv,
                                effortValue,
                                baseStat,
                                pokemon.getNature().getStat(stat));

                        damageRanges.add(calculate(range.getAttackerLevel(),
                                (int) Math.floor(range.getStat() * range.getAttackModifier()),
                                (int) Math.floor(effectiveStat * range.getDefenseModifier()),
                                ranges.getCrit(),
                                range,
                                stat,
                                iv));
                    }
                }
            }

            for (SpeedRange range : ranges.getSpeedRanges()) {
                Map<Stat, Integer> effortValues = EffortValueProvider.getEffortValues(range.getLevel());
                for (int iv : List.of(pokemon.getMinIndividualValues().get(Stat.SPD).get(),pokemon.getMaxIndividualValues().get(Stat.SPD).get())) {
                    int effectiveStat = getExpectedStat(range.getLevel(),
                            iv,
                            effortValues.get(Stat.SPD),
                            pokemon.pokemonModelFromFileProperty().get().getBaseStats().getSpd(),
                            pokemon.getNature().getStat(Stat.SPD));
                }
            }
        }
        damageRanges.sort(Comparator.comparingInt(o -> o.getRange().getPos()));
        return damageRanges;
    }

    private Damage calculate(int level, int attack, int defense, double crit, Range range, Stat stat, int iv) {
        double baseCalc = (Math.floor((Math.floor((2 * level) / 5.0 + 2) * range.getMovePower() * attack) / defense) / 50 + 2) * range.getModifier();
        return new Damage(
                range,
                iv,
                stat,
                (int) Math.floor(baseCalc * 0.85),
                (int) Math.floor(baseCalc),
                (int) Math.floor(baseCalc * 0.85 * crit),
                (int) Math.floor(baseCalc * crit)
        );
    }

    public static int getExpectedStat(int level, int iv, int ev, int baseValue, int nature) {
        int statValue = (2 * baseValue + iv + ev / 4) * level / 100 + 5;
        if (nature == 1) {
            statValue *= 1.1;
        } else if (nature == -1) {
            statValue *= 0.9;
        }
        return statValue;
    }
}
