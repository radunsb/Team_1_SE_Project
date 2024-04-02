package classes;

import classes.Course;
import classes.Major;
import classes.Minor;
import classes.Schedule;

import java.util.ArrayList;

public class Student {
    private int studentID;
    private String username;
    private Class classStanding; //See below: enum Class
    enum Class{
        FRESHMAN, SOPHOMORE, JUNIOR, SENIOR, OTHER
    }
    private ArrayList<Major> majors;
    private ArrayList<Minor> minors;
    private ArrayList<Course> courseHistory; //Already taken classes
    private ArrayList<Schedule> schedules; //Students created schedules


    public Student(int studentID,
                   String username,
                   Class classStanding,
                   ArrayList<Major> majors,
                   ArrayList<Minor> minors,
                   ArrayList<Course> courseHistory,
                   ArrayList<Schedule> schedules) {
        this.studentID = studentID;
        this.username = username;
        this.classStanding = classStanding;
        this.majors = majors;
        this.minors = minors;
        this.courseHistory = courseHistory;
        this.schedules = schedules;
    }

    /**
     * Adds the specified classes.Course object to the courseHistory ArrayList
     * @param course is the course to add
     */
    public void addCourseToHistory(Course course){

    }

    /**
     * Removes the specified classes.Course object from the courseHistory ArrayList
     * @param course is the course to remove
     */
    public void removeCourseFromHistory(Course course){

    }

    public void addNewSchedule(int scheduleID, String semester, int year, String scheuleName){
        Schedule s = new Schedule(scheduleID,semester, year,"mySchedule");
        schedules.add(s);
    }

    /**
     * Adds the specified classes.Major object to the majors ArrayList
     * @param major is the specified major to add
     */
    public void addMajor(Major major){

    }

    /**
     * Removes the specified classes.Major object from the majors ArrayList
     * @param major is the specified major to remove
     */
    public void removeMajor(Major major){

    }

    /**
     * Adds the specified classes.Minor object to the majors ArrayList
     * @param minor is the specified minor to add
     */
    public void addMinor(Minor minor){

    }

    /**
     * Removes the specified classes.Minor object from the majors ArrayList
     * @param minor is the specified minor to remove
     */
    public void removeMinor(Minor minor){

    }

    public int getStudentID() {
        return studentID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Class getClassStanding() {
        return classStanding;
    }

    public void setClassStanding(Class classStanding) {
        this.classStanding = classStanding;
    }

    public ArrayList<Major> getMajors() {
        return majors;
    }

    public void setMajors(ArrayList<Major> majors) {
        this.majors = majors;
    }

    public ArrayList<Minor> getMinors() {
        return minors;
    }

    public void setMinors(ArrayList<Minor> minors) {
        this.minors = minors;
    }

    public ArrayList<Course> getCourseHistory() {
        return courseHistory;
    }

    public void setCourseHistory(ArrayList<Course> courseHistory) {
        this.courseHistory = courseHistory;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }
}
