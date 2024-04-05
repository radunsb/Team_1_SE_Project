package classes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    private int year;
    enum Semester{
        FALL, SPRING
    }
    private Semester semester;
    private int creditHrs;
    private int capacity;


    public Course(String courseCode,
                  String name,
                  String description,
                  String professor,
                  Date[][] meetingTimes,
                  boolean[] meetingDays,
                  int year,
                  int creditHrs,
                  Semester semester,
                  int capacity,
                  ArrayList<String> meetingLocations,
                  ArrayList<String> prerequisites,
                  ArrayList<String> corequisites) {
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.professor = professor;
        this.meetingTimes = meetingTimes;
        this.meetingDays = meetingDays;
        this.year = year;
        this.capacity = capacity;
        this.creditHrs = creditHrs;
        this.semester = semester;
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

    public void setCorequisites(ArrayList<String> corequisites) { this.corequisites = corequisites; }

    public String toString() {
        String code = getCourseCode();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        if(getMeetingTimes() != null) {
            for(int i = 0; i < 5; i++) {
                if(getMeetingTimes()[i][0] != null) {
                    return "[" + code + "-" + dateFormat.format(getMeetingTimes()[i][0]) + ":" + dateFormat.format(getMeetingTimes()[i][1]) + "] ";
                }
            }
        }
        return "[" + code + "] ";
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getCreditHrs() {
        return creditHrs;
    }

    public void setCreditHrs(int creditHrs) {
        this.creditHrs = creditHrs;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
