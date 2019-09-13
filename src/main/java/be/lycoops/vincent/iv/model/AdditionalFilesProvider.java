package be.lycoops.vincent.iv.model;

public class AdditionalFilesProvider {

    private static String routeDir = "";

    private static String pokemonDir = "";

    private static String rangesDir = "";

    public static String getRouteDir() {
        return routeDir;
    }

    public static void setRouteDir(String routeDir) {
        AdditionalFilesProvider.routeDir = routeDir;
    }

    public static String getPokemonDir() {
        return pokemonDir;
    }

    public static void setPokemonDir(String pokemonDir) {
        AdditionalFilesProvider.pokemonDir = pokemonDir;
    }

    public static String getRangesDir() {
        return rangesDir;
    }

    public static void setRangesDir(String rangesDir) {
        AdditionalFilesProvider.rangesDir = rangesDir;
    }
}
