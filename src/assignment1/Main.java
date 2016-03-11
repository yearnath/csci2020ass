package assignment1;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class Main extends Application {
    private BorderPane layout;
    private TableView<SpamHam> table;
    private int accuracy = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Assignment 1");

        //Initializes the directoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        //creates a tableView for the upper 3/4 of the application
        table = new TableView<>();

        //adds spam words to the trainSpamFreq map
        Map<String, Double> trainSpamFreq = new HashMap<>();
        File spamFile = new File(mainDirectory.getName() + "/train/spam");
        System.out.println(spamFile.getName());
        assignment1.DataSource.getAllSpamHam(spamFile, trainSpamFreq);
        System.out.println(trainSpamFreq);

        //adds ham words to the trainHamFreq map
        Map<String, Double> trainHamFreq = new HashMap<>();
        File hamFile1 = new File(mainDirectory.getName() + "/train/ham");
        File hamFile2 = new File(mainDirectory.getName() + "/train/ham2");
        assignment1.DataSource.getAllSpamHam(hamFile1, trainHamFreq);
        assignment1.DataSource.getAllSpamHam(hamFile2, trainHamFreq);
        System.out.println(trainHamFreq);

        //divide map values by number of files
        trainSpamFreq.replaceAll((k,v) -> v/spamFile.length());
        trainHamFreq.replaceAll((k,v) -> v/(hamFile1.length() + hamFile2.length()));

        //initializes SpamChance map
        Map<String, Double> spamChance = createSpamChanceMap(trainHamFreq, trainSpamFreq);

        //add test emails to table
        File testFile = new File(mainDirectory.getName() + "/test/spam");
        ObservableList<SpamHam> spamHams = assignment1.DataSource.test(testFile, spamChance, "Spam");
        File testFile2 = new File(mainDirectory + "/test/ham");
        spamHams.addAll(assignment1.DataSource.test(testFile2, spamChance, "Ham"));
        table.setItems(spamHams);

        TableColumn<SpamHam, String> nameColumn;
        nameColumn = new TableColumn<>("File");
        nameColumn.setMinWidth(500);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));

        TableColumn<SpamHam, String> classColumn;
        classColumn = new TableColumn<>("Actual Class");
        classColumn.setMinWidth(100);
        classColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));

        TableColumn<SpamHam, String> probabilityColumn;
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
        accField.setText((getAccuracy(spamHams)));
        precisionArea.add(accField, 1, 0);

        Label precLabel = new Label("Precision:");
        precisionArea.add(precLabel, 0, 1);
        TextField precField = new TextField();
        precField.setText((getPrecision(spamHams)));
        precisionArea.add(precField, 1, 1);

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
                                    (HamFreq.get(words[i])+SpamFreq.getOrDefault(words[i],0.0))));
        }
        Set<String> words2 = SpamFreq.keySet();
        words = words2.toArray(new String[words2.size()]);
        for (int i = 0; i < words.length; i++)
        {
            spamChance.putIfAbsent(words[i],(SpamFreq.get(words[i])/
                                            (HamFreq.getOrDefault(words[i],0.0)+SpamFreq.get(words[i]))));
        }
        return spamChance;
    }

    private String getAccuracy(ObservableList<SpamHam> spamHams)
    {
        double acc = 0;
        double total = 0;
        double correct = 0;
        for (SpamHam sh : spamHams){
            if (sh.getAcc())
            {
                correct++;
            }
            total++;
        }
        acc = correct/total;
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(acc);
    }

    private String getPrecision(ObservableList<SpamHam> spamHams)
    {
        double pre = 0;
        double guessSpam = 0;
        double guessHam = 0;
        double correctSpam = 0;
        double correctHam = 0;

        for (SpamHam sh : spamHams){
            if (sh.getSpamProbability() > 0.5)
            {
                guessSpam++;
                if (sh.getActualClass().equals("Spam"))
                {
                    correctSpam++;
                }
            }
            else
            {
                guessHam++;
                if (sh.getActualClass().equals("Ham"))
                {
                    correctHam++;
                }
            }
        }
        pre = (correctSpam+correctHam)/(guessSpam+guessHam);
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(pre);
    }
}


