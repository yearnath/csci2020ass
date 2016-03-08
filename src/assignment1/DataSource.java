package assignment1;
import javafx.collections.*;

public class DataSource {
    public static ObservableList<SpamHam> getAllSpamHams() {
        ObservableList<SpamHam> spamHams = FXCollections.observableArrayList();

        spamHams.add(new SpamHam("Hell yeah", 3, "as"));
        spamHams.add(new SpamHam("bitch", 1, "hell"));
        spamHams.add(new SpamHam("this", 4, " "));
        spamHams.add(new SpamHam("go", 1, " "));
        spamHams.add(new SpamHam("hard", 5, "FLOCKA"));

        return spamHams;
    }
}