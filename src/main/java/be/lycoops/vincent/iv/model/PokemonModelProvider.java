package be.lycoops.vincent.iv.model;

import be.lycoops.vincent.iv.model.json.PokemonModel;
import be.lycoops.vincent.iv.model.json.PokemonStats;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PokemonModelProvider {

    public static PokemonModel loadPokemonFile() {
        System.out.println("loading pokemon from file");

        if (EffortValueProvider.getRoute() != null) {
            String pokemonDir = AdditionalFilesProvider.getPokemonDir();
            String pokemonFile = "";
            if (!pokemonDir.isEmpty()) {
                pokemonFile = String.format("%s/%s.json", pokemonDir, EffortValueProvider.getRoute());
            } else {
                pokemonFile = String.format("be/lycoops/vincent/iv/pokemon/%s.json", EffortValueProvider.getRoute());
            }
            Path path = FilePathProvider.getPath(pokemonFile);
            if (path == null) {

                if (EffortValueProvider.getRoute().contains("-")) {
                    if (!pokemonDir.isEmpty()) {
                        pokemonFile = String.format("%s/%s.json", pokemonDir, EffortValueProvider.getRoute().split("-")[0]);
                    } else {
                        pokemonFile = String.format("be/lycoops/vincent/iv/pokemon/%s.json", EffortValueProvider.getRoute().split("-")[0]);
                    }
                }
                path = FilePathProvider.getPath(pokemonFile);
                if (path == null) {
                    pokemonFile = String.format("be/lycoops/vincent/iv/pokemon/%s.json", EffortValueProvider.getRoute());
                    path = FilePathProvider.getPath(pokemonFile);

                    if (path == null) {
                        if (EffortValueProvider.getRoute().contains("-")) {
                            pokemonFile = String.format("be/lycoops/vincent/iv/pokemon/%s.json", EffortValueProvider.getRoute().split("-")[0]);

                            path = FilePathProvider.getPath(pokemonFile);

                            if (path == null) {
                                System.out.println("pokemon file could not be loaded for route: " + EffortValueProvider.getRoute());
                            }
                        } else {
                            System.out.println("pokemon file could not be loaded for route: " + EffortValueProvider.getRoute());
                        }
                    }
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
                System.out.println("pokemon could not be loaded");
            } else {
                Gson g = new Gson();

                return g.fromJson(json, PokemonModel.class);
            }
        }
        return new PokemonModel(0,0, "",
                new PokemonStats(0,0,0,0,0,0),
                new PokemonStats(0,0,0,0,0,0),
                new PokemonStats(0,0,0,0,0,0));
    }
}
