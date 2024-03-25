package classes;

import classes.Course;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Schedule {
    private int scheduleID;
    private String semester;
    private String scheduleName;
    private ArrayList<Course> courses;
    //alex is nice

    public Schedule(int scheduleID, String semester, String scheduleName) {
        this.scheduleID = scheduleID;
        this.semester = semester;
        this.scheduleName = scheduleName;
        this.courses = new ArrayList<>();
    }

    /**
     * Adds the specified classes.Course object to the courses ArrayList
     * @param course is the course to add to the schedule
     */
    public void addCourse(Course course){
        courses.add(course);
    }

    /**
     * Removes the specified classes.Course object from the courses ArrayList
     * @param course is the course to remove from the schedule
     */
    public void removeCourse(Course course){
    String courseCode = course.getCourseCode();

    }

    /**
     * toString method for a schedule.
     * @return a string representation of a schedule in a weekly timeslot format
     */
    public String toString(){

        StringBuilder str = new StringBuilder();

        return null;
    }

    public void saveSchedule() throws FileNotFoundException {
        //save the current schedule to a csv file
        File csvOutputFile = new File(scheduleID + "_" + scheduleName);
        ArrayList<String> codes = new ArrayList<String>();
        for(Course course : courses){
            codes.add(course.getCourseCode());
        }
        String codeString = String.join("$", codes);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)){
            pw.print(scheduleID + "$" + semester + "$" + scheduleName + "$" + codeString);
        }
    }

    public void loadSchedule(File csvToParse) throws FileNotFoundException {
        Scanner inScan = new Scanner(csvToParse);
        while(inScan.hasNext()){
            //blah blah do stuff
            inScan.next();
        }
        //read through the csv, setting the attributes of the Schedule to the parsed contents
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
