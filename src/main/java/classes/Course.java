package classes;

import java.util.ArrayList;
import java.util.Date;

public class Course {
    private String courseCode;
    private String name;
    private String description;
    private String professor;
    private Date[][] meetingTimes;
    private boolean[] meetingDays;
    private ArrayList<String> meetingLocations;
    private ArrayList<String> prerequisites; //ArrayList of course codes
    private ArrayList<String> corequisites; //ArrayList of course codes


    public Course(String courseCode,
                  String name,
                  String description,
                  String professor,
                  Date[][] meetingTimes,
                  boolean[] meetingDays,
                  ArrayList<String> meetingLocations,
                  ArrayList<String> prerequisites,
                  ArrayList<String> corequisites) {
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.professor = professor;
        this.meetingTimes = meetingTimes;
        this.meetingDays = meetingDays;
        this.meetingLocations = meetingLocations;
        this.prerequisites = prerequisites;
        this.corequisites = corequisites;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public Date[][] getMeetingTimes() {
        return meetingTimes;
    }

    public void setMeetingTimes(Date[][] meetingTimes) {
        this.meetingTimes = meetingTimes;
    }

    public boolean[] getMeetingDays(){
        return meetingDays;
    }
    public void setMeetingDays(boolean[] meetingDays){
        this.meetingDays = meetingDays;
    }

    public ArrayList<String> getMeetingLocations() {
        return meetingLocations;
    }

    public void setMeetingLocations(ArrayList<String> meetingLocations) {
        this.meetingLocations = meetingLocations;
    }

    public ArrayList<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(ArrayList<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public ArrayList<String> getCorequisites() {
        return corequisites;
    }

    public void setCorequisites(ArrayList<String> corequisites) {
        this.corequisites = corequisites;
    }
}
