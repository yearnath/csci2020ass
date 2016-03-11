package assignment1;
import javafx.collections.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataSource {
    public static void getAllSpamHam(File file, Map<String, Double> spamHam) throws IOException{
        Map<String, Double> current = new HashMap<>();
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (File temp : filesInDir) {
                getAllSpamHam(temp, spamHam);
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);

            //for each word in a given file, checks if the word is in the current and spamHam
            //if the word is in both, nothing happens
            //if it's in neither, it's added to both
            //if it's only not in current, the value for that key is +1 in the spamHam map
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

            double n = 0.0;
            //a variable that increases accuracy by checking for run on spam
            int Schecker = 0;
            while (scanner.hasNext()) {
                String word = (scanner.next()).toLowerCase();
                if (isWord(word)) {
                    //uses default so there are no null values
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
                        Schecker++;
                        //if checker is greater than 5 its a spam sentence, add -11 (max spam value)
                        //this essentially doubles the spam rating for this word
                        if (Schecker > 5)
                        {
                            chance = 0.99999;
                            n += -11;
                        }
                        //if checker is more than 1 greater than it is at the start of a spam statement,
                        //increase spam rating
                        else if (Schecker > 1)
                        {
                            chance = 0.99999;
                        }
                    }
                    //word probably isn't in a spam email, reduce checker to 0
                    else if (chance < 0.5)
                    {
                        Schecker = 0;
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