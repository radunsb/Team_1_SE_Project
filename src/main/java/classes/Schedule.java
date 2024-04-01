package classes;

import classes.Course;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.module.FindException;
import java.util.*;

import java.util.ArrayList;

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

    /**
     * Takes the data within the current schedule, and saves it to a csv file so that
     * it can be reloaded at a later date
     */
    public void saveSchedule(){
        //save the current schedule to a csv file
        File csvOutputFile = new File(scheduleID + "_" + scheduleName);
        ArrayList<String> codes = new ArrayList<>();
        for(Course course : courses){
            codes.add(course.getCourseCode());
        }
        String codeString = String.join("$", codes);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)){
            pw.print(scheduleID + "$" + semester + "$" + scheduleName + "$" + codeString);
        }
        catch(FileNotFoundException fe){
            System.out.println("The software was unable to to save your schedule");
        }
    }

    /**
     * Takes in a File and sets the current Schedule parameters to those specified
     * by the file
     * @param csvToParse File with the Schedule data to load
     */
    public void loadSchedule(File csvToParse) {
        Scanner inScan;
        int scheduleID;
        String semester;
        String scheduleName;
        ArrayList<String> parts = new ArrayList<>();
        try {
            inScan = new Scanner(csvToParse);
        } catch (FileNotFoundException fe) {
            System.out.println("The specified schedule does not exist on the system");
            return;
        }
        while (inScan.hasNext()) {
            parts.add(inScan.next());
        }
        if(parts.size() < 3){
            System.out.println("The specified schedule is in an unsupported format");
            return;
        }
        Main main = new Main();
        ArrayList<Filter> filt = new ArrayList<>();

        Search search = new Search("", main.getCourseCatalog(), filt);
        scheduleID = Integer.parseInt(parts.get(0));
        semester = parts.get(1);
        scheduleName = parts.get(2);
        Filter semFilter = new Filter((ArrayList<String>) Collections.singletonList(semester), Filter.FilterType.SEMESTER);
        filt.add(semFilter);
        ArrayList<String> coursePrimaryKeys = (ArrayList<String>) parts.subList(3, parts.size());
        ArrayList<Course> courses = (ArrayList<Course>) coursePrimaryKeys.stream().map(course -> {
            search.search(course);
            search.search(filt);
            if(search.getResults().size() > 1){
                throw new FindException("More than one entry found for a certain class. Schedule was unable to be loaded");
            }
            else if(search.getResults().isEmpty()){
                throw new FindException("The saved schedule contains a Course that could not be found in the database. Schedule was unable to be loaded");
            }
            return search.getResults().get(0);
        }).toList();
        this.scheduleID = scheduleID;
        this.semester = semester;
        this.scheduleName = scheduleName;
        this.courses = courses;
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
