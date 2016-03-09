package assignment1;
import javafx.collections.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataSource {
    public static Map<String, Integer> getAllSpamHam(File file) throws IOException{
        Map<String, Integer> spamHam = new HashMap<String, Integer>();
        Map<String, Integer> current = new HashMap<String, Integer>();
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                spamHam.putAll(getAllSpamHam(filesInDir[i]));
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
                        spamHam.put(word,1);
                        current.put(word,1);
                    }
                    else if (spamHam.containsKey(word) && current.containsKey(word))
                    {
                        spamHam.put(word,spamHam.get(word)+1);
                        current.put(word,1);
                    }
                }
            }
        }
        return spamHam;
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