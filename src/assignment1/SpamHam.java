package assignment1;
import java.text.DecimalFormat;

public class SpamHam {
    private String filename;
    private double spamProbability;
    private String actualClass;

    public SpamHam(String filename, double spamProbability, String actualClass) {
        this.filename = filename;
        this.spamProbability = spamProbability;
        this.actualClass = actualClass;
        //System.out.println(filename + " : " + getSpamProbRounded() + " : " + getSpamProbability());
    }

    public String getFilename() { return this.filename; }
    public double getSpamProbability() { return this.spamProbability; }

    public String getSpamProbRounded() {
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(this.spamProbability);
    }

    public String getActualClass() { return this.actualClass; }
    public void setFilename(String value) { this.filename = value; }
    public void setSpamProbability(double val) { this.spamProbability = val; }
    public void setActualClass(String value) { this.actualClass = value; }

    public int getAcc(){
        if (actualClass == "Spam" && spamProbability >= 0.5)
            return 1;
        else if (actualClass == "Ham" && spamProbability <= 0.5)
            return 1;
        else
            return 0;
    }
}