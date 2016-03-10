package assignment1;
import javafx.beans.InvalidationListener;
import javafx.collections.*;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataSource {
    public static void getAllSpamHam(File file, Map<String, Double> spamHam) throws IOException{
        Map<String, Double> current = new HashMap<>();
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            double numFiles = filesInDir.length;
            for (int i = 0; i < filesInDir.length; i++) {
                getAllSpamHam(filesInDir[i], spamHam);
            }
            spamHam.replaceAll((k,v) -> v/numFiles);
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
            double n = 0;
            while (scanner.hasNext()) {
                String word = (scanner.next()).toLowerCase();
                if (isWord(word)) {
                    n += (Math.log(1.0 - spamChance.getOrDefault(word, 0.0)) - Math.log(spamChance.getOrDefault(word, 0.0)));
                }
            }
            double prob = (1/(1 + Math.pow(Math.E,n)));
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