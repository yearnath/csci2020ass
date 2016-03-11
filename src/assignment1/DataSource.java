package assignment1;
import javafx.beans.InvalidationListener;
import javafx.collections.*;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class DataSource {
    public static void getAllSpamHam(File file, Map<String, Double> spamHam) throws IOException{
        Map<String, Double> current = new HashMap<>();
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                getAllSpamHam(filesInDir[i], spamHam);
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);

            //for each word in a given file, checks if the word is in the current and spamHam
            //if the word is in the current file and spam
            while (scanner.hasNext()) {
                String word = (scanner.next()).toLowerCase();
                if (isWord(word)) {
                    if (!spamHam.containsKey(word) && !current.containsKey(word))
                    {
                        spamHam.put(word,1.0);
                        current.put(word,1.0);
                    }
                    else if (spamHam.containsKey(word) && !current.containsKey(word))
                    {
                        spamHam.put(word,spamHam.get(word)+1.0);
                        current.put(word,1.0);
                    }
                }
            }
        }
    }

    public static ObservableList<SpamHam> test(File file, Map<String, Double> spamChance, String aClass) throws IOException
    {
        ObservableList<SpamHam> spamHams = FXCollections.observableArrayList();
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                spamHams.addAll(test(filesInDir[i], spamChance, aClass));
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);

            //for each word in a given file, checks if the word is in the current and spamHam
            //if the word is in the current file and spam
            double n = 0.0;
            //a variable that increases accuracy by checking for run on spam
            int checker = 0;
            while (scanner.hasNext()) {
                String word = (scanner.next()).toLowerCase();
                if (isWord(word)) {
                    double chance = spamChance.getOrDefault(word, 0.00001);
                    //to avoid -infinity
                    if (chance == 1) {
                        chance = 0.99999;
                    }
                    //to avoid infinity
                    else if (chance == 0) {
                        chance = 0.00001;
                    }
                    //word is probably in a spam email, add 1 to checker
                    if (chance > 0.5)
                    {
                        checker++;
                        //if checker is greater than 3 its a "spam statement" make chance 0.99999
                        if (checker > 3)
                        {
                            chance = 0.99999;
                        }
                    }
                    //word probably isn't in a spam email, reduce checker to 0
                    else if (chance < 0.5)
                    {
                        checker = 0;
                    }
                    n += Math.log(1.0 - chance) - Math.log(chance);
                }
            }
            double prob = (1.0/(1.0 + Math.pow(Math.E,n)));
            SpamHam sh = new SpamHam(file.getName(), prob, aClass);
            spamHams.add(sh);
        }
        return spamHams;
    }

    //determines if given string is a word
    private static boolean isWord(String str){
        String pattern = "^[a-z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }
}