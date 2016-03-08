package assignment1;
import javafx.collections.*;

public class DataSource {
    public static ObservableList<SpamHam> getAllSpamHams() {
        ObservableList<SpamHam> spamHams = FXCollections.observableArrayList();

        spamHams.add(new SpamHam("Testing", 1, "Status"));
        spamHams.add(new SpamHam("Testing1", 2, "Status1"));
        spamHams.add(new SpamHam("Testing2", 3, "Status2"));
        spamHams.add(new SpamHam("Testing3", 4, "Status3"));
        spamHams.add(new SpamHam("Testing4", 5, "Status4"));

        return spamHams;
    }
}