package assignment1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main extends Application {
    private BorderPane layout;
    private TableView<SpamHam> table;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Assignment 1");

        //creates a tableView for the upper 3/4 of the application
        table = new TableView<>();

        //chooses reference files for spam/ham training
        File file1 = new File("data/train/spam"); File file2 = new File("data/train/ham"); File file3 = new File("data/train/ham2");

        //adds spam words to the trainSpamFreq map
        Map<String, Double> trainSpamFreq = new HashMap<>();
        assignment1.DataSource.getAllSpamHam(file1, trainSpamFreq);

        //adds ham words to the trainHamFreq map
        Map<String, Double> trainHamFreq = new HashMap<>();
        assignment1.DataSource.getAllSpamHam(file2, trainHamFreq);
        assignment1.DataSource.getAllSpamHam(file3, trainHamFreq); //add files from ham to ass well

        //initializes SpamChance map

        Map<String, Double> spamChance = createSpamChanceMap(trainHamFreq, trainSpamFreq);
        File testFile = new File("data/test/spam");
        table.setItems(assignment1.DataSource.test(testFile, spamChance, "Spam"));

        TableColumn<SpamHam, String> nameColumn = null;
        nameColumn = new TableColumn<>("File");
        nameColumn.setMinWidth(500);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));

        TableColumn<SpamHam, String> classColumn = null;
        classColumn = new TableColumn<>("Actual Class");
        classColumn.setMinWidth(100);
        classColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));

        TableColumn<SpamHam, String> probabilityColumn = null;
        probabilityColumn = new TableColumn<>("Spam Probability");
        probabilityColumn.setMinWidth(300);
        probabilityColumn.setCellValueFactory(new PropertyValueFactory<>("spamProbRounded"));

        table.getColumns().add(nameColumn);
        table.getColumns().add(classColumn);
        table.getColumns().add(probabilityColumn);

        GridPane precisionArea = new GridPane();
        precisionArea.setPadding(new Insets(10, 10, 10, 10));
        precisionArea.setVgap(10);
        precisionArea.setHgap(10);

        Label accLabel = new Label("Accuracy:");
        precisionArea.add(accLabel, 0, 0);
        TextField accField = new TextField();
        precisionArea.add(accField, 1, 0);

        Label precLabel = new Label("Precision:");
        precisionArea.add(precLabel, 0, 1);
        TextField precField = new TextField();
        precisionArea.add(precField, 1, 1);

        accField.setText("Unknown");
        precField.setText("Unknown");

        accField.setEditable(false);
        precField.setEditable(false);

        layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(precisionArea);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    //creates SpamChance map
    private Map<String, Double> createSpamChanceMap(Map<String, Double> HamFreq, Map<String, Double> SpamFreq)
    {
        Map<String, Double> spamChance = new HashMap<>();
        Set<String> words1 = HamFreq.keySet();
        String[] words = words1.toArray(new String[words1.size()]);
        for (int i = 0; i < words.length; i++)
        {
            spamChance.put(words[i],(SpamFreq.getOrDefault(words[i],0.0)/
                                    (HamFreq.getOrDefault(words[i],0.0)+SpamFreq.getOrDefault(words[i],0.0))));
        }
        Set<String> words2 = SpamFreq.keySet();
        words = words2.toArray(new String[words2.size()]);
        for (int i = 0; i < words.length; i++)
        {
            spamChance.putIfAbsent(words[i],(SpamFreq.getOrDefault(words[i],0.0)/
                                            (HamFreq.getOrDefault(words[i],0.0)+SpamFreq.getOrDefault(words[i],0.0))));
        }
        return spamChance;
    }
}


