package classes;

import java.util.ArrayList;

public class Major {
    private int majorID;
    private String majorName;
    private ArrayList<String> coreRequirements; //ArrayList of course codes

    public Major(int majorID, String majorName, ArrayList<String> coreRequirements) {
        this.majorID = majorID;
        this.majorName = majorName;
        this.coreRequirements = coreRequirements;
    }

    public int getMajorID() {
        return majorID;
    }

    public void setMajorID(int majorID) {
        this.majorID = majorID;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public ArrayList<String> getCoreRequirements() {
        return coreRequirements;
    }

    public void setCoreRequirements(ArrayList<String> coreRequirements) {
        this.coreRequirements = coreRequirements;
    }
}
