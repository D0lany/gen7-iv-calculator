package be.lycoops.vincent.iv.calculator.reset;

import be.lycoops.vincent.iv.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class ResetPresenter implements Initializable {

    private Preferences prefs;

    private static String routePref = "ROUTE_PREF";
    private static String askOnChangePref = "ASK_ON_CHANGE_PREF";

    @FXML
    private ComboBox<String> routeSelect;

    @FXML
    private CheckBox askOnChangeButton;

    @FXML
    private TextArea ranges;

    @Inject
    private Pokemon pokemon;

    @Inject
    private RangesCalculator rangesCalculator;

    @Inject
    private HiddenPowerCalculator hiddenPowerCalculator;

    @Inject
    private NatureCalculator natureCalculator;

    @Inject
    private History history;

    public void reset() {
        if (!askOnChangeButton.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Route Change");
            alert.setHeaderText("Do you really want to change the route?");
            alert.setContentText("This resets all current progress");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                doReset();
            }
        } else {
            doReset();
        }
    }

//    TODO make view better
    public void calculateRanges() {
        ranges.clear();
        List<Damage> damages = rangesCalculator.calculateAll();
        for (Damage damage : damages) {
            ranges.appendText(damage.toString());
        }
    }

    public void askOnChangeAction() {
        prefs.put(askOnChangePref, String.valueOf(askOnChangeButton.isSelected()));
    }

    private void doReset() {
        String route = routeSelect.getSelectionModel().getSelectedItem();
        if (route == null || route.isEmpty()) {
            routeSelect.getSelectionModel().select(0);
        }
        EffortValueProvider.setRoute(route);

        prefs.put(routePref, route);

        ranges.clear();
        pokemon.reset();
        natureCalculator.reset();
        pokemon.setHiddenPower(hiddenPowerCalculator.setUnknown());
        rangesCalculator.loadRanges();
        history.reset();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prefs = Preferences.userRoot().node(this.getClass().getName());

        askOnChangeButton.setSelected(prefs.getBoolean(askOnChangePref,false));
//        ranges.setDisable(true);

        routeSelect.getItems().removeAll(routeSelect.getItems());

        String routeDir = AdditionalFilesProvider.getRouteDir();

        List<Stream<Path>> walk = new ArrayList<>();
        try {
            Stream<Path> walk1 = Files.walk(FilePathProvider.getPath("be/lycoops/vincent/iv/routes"), 1);
            if (!isNull(walk1)) {
                walk.add(walk1);
            }
            if (!routeDir.isEmpty()) {
                Stream<Path> walk2 = Files.walk(FilePathProvider.getPath(routeDir), 1);
                if (!isNull(walk2)) {
                    walk.add(walk2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Stream<Path> w : walk) {
            for (Iterator<Path> it = w.iterator(); it.hasNext(); ) {
                String route = it.next().getFileName().toString().split("\\.txt")[0];
                if (route.equals("routes")) continue;
                routeSelect.getItems().add(route);
                if (route.equals(prefs.get(routePref, ""))) {
                    routeSelect.getSelectionModel().select(route);
                }
            }
        }

        doReset();
    }

}
