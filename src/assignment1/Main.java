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

import javax.activation.DataSource;
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

        table = new TableView<>();
        File file1 = new File("spam"); File file2 = new File("ham"); File file3 = new File("ham2");
        Map<String, Integer> spamFreq = (assignment1.DataSource.getAllSpamHam(file1));
        Map<String, Integer> hamFreq = (assignment1.DataSource.getAllSpamHam(file2));
        hamFreq.putAll(assignment1.DataSource.getAllSpamHam(file3));

        Map<String, Double> SpamChance = spamChance(hamFreq, spamFreq);

        TableColumn<SpamHam, String> nameColumn = null;
        nameColumn = new TableColumn<>("File");
        nameColumn.setMinWidth(500);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));

        TableColumn<SpamHam, String> classColumn = null;
        classColumn = new TableColumn<>("Actual Class");
        classColumn.setMinWidth(100);
        classColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));

        TableColumn<SpamHam, Double> probabilityColumn = null;
        probabilityColumn = new TableColumn<>("Spam Probability");
        probabilityColumn.setMinWidth(200);
        probabilityColumn.setCellValueFactory(new PropertyValueFactory<>("spamProbability"));

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

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    Map<String, Double> spamChance(Map<String, Integer> HamFreq, Map<String, Integer> SpamFreq)
    {
        Map<String, Double> SpamChance = new HashMap<>();
        Set<String> words1 = HamFreq.keySet();
        String[] words = words1.toArray(new String[words1.size()]);
        for (int i = 0; i < words.length; i++)
        {
            SpamChance.put(words[i],(SpamFreq.getOrDefault(words[i],0)/
                                    (HamFreq.getOrDefault(words[i],0)*1.0+SpamFreq.getOrDefault(words[i],0))));
        }
        Set<String> words2 = SpamFreq.keySet();
        words = words1.toArray(new String[words1.size()]);
        for (int i = 0; i < words.length; i++)
        {
            SpamChance.putIfAbsent(words[i],(SpamFreq.getOrDefault(words[i],0)/
                                            (HamFreq.getOrDefault(words[i],0)*1.0+SpamFreq.getOrDefault(words[i],0))));
        }
        return SpamChance;
    }
}


