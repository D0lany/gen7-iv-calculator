package be.lycoops.vincent.iv.calculator.additionalfiles;


import be.lycoops.vincent.iv.model.AdditionalFilesProvider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class AdditionalfilesPresenter implements Initializable {

    private Preferences prefs;

    private static String routeDirPref = "ROUTE_DIR_PREF";
    private static String pokemonDirPref = "POKEMON_DIR_PREF";
    private static String rangesDirPref = "RANGES_DIR_PREF";

    @FXML
    private TextField routeDir;

    @FXML
    private TextField pokemonDir;

    @FXML
    private TextField rangesDir;

    @FXML
    private Button dirButton;

    @FXML
    public void updateDirs() {
        prefs.put(routeDirPref, String.valueOf(routeDir.getText()));
        prefs.put(pokemonDirPref, String.valueOf(pokemonDir.getText()));
        prefs.put(rangesDirPref, String.valueOf(rangesDir.getText()));

        AdditionalFilesProvider.setRouteDir(String.valueOf(routeDir.getText()));
        AdditionalFilesProvider.setPokemonDir(String.valueOf(pokemonDir.getText()));
        AdditionalFilesProvider.setRangesDir(String.valueOf(rangesDir.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prefs = Preferences.userRoot().node(this.getClass().getName());

        routeDir.setText(prefs.get(routeDirPref,""));
        pokemonDir.setText(prefs.get(pokemonDirPref,""));
        rangesDir.setText(prefs.get(rangesDirPref,""));

        AdditionalFilesProvider.setRouteDir(String.valueOf(routeDir.getText()));
        AdditionalFilesProvider.setPokemonDir(String.valueOf(pokemonDir.getText()));
        AdditionalFilesProvider.setRangesDir(String.valueOf(rangesDir.getText()));
    }
}
