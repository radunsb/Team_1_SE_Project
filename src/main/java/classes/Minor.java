package classes;

import java.util.ArrayList;

public class Minor {
    private int minorID;
    private String minorName;
    private ArrayList<String> coreRequirements; //ArrayList of course codes

    public Minor(int minorID, String minorName, ArrayList<String> coreRequirements) {
        this.minorID = minorID;
        this.minorName = minorName;
        this.coreRequirements = coreRequirements;
    }

    public int getMinorID() {
        return minorID;
    }

    public void setMinorID(int minorID) {
        this.minorID = minorID;
    }

    public String getMinorName() {
        return minorName;
    }

    public void setMinorName(String minorName) {
        this.minorName = minorName;
    }

    public ArrayList<String> getCoreRequirements() {
        return coreRequirements;
    }

    public void setCoreRequirements(ArrayList<String> coreRequirements) {
        this.coreRequirements = coreRequirements;
    }
}
