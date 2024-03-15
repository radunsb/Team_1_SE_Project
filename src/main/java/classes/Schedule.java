package classes;

import classes.Course;
import java.util.*;

import java.util.ArrayList;

import static classes.Student.Class.JUNIOR;

public class Schedule {
    private int scheduleID;
    private String semester;
    private String scheduleName;
    private ArrayList<Course> courses;


    public Schedule(int scheduleID, String semester, String scheduleName) {
        this.scheduleID = scheduleID;
        this.semester = semester;
        this.scheduleName = scheduleName;
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
        for(int i = 0; i < courses.size(); i++){
            if(courses.get(i).getCourseCode().equals(courseCode)){
                courses.remove(i);
            }
        }
    }

    /**
     * toString method for a schedule.
     * @return a string representation of a schedule in a weekly timeslot format
     */
    public String toString(){
    StringBuilder str = new StringBuilder();
    str.append("Schedule ID: |");
    str.append(scheduleID);
    str.append(" |");
        return str.toString();
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getSemester() {
        //hello
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




//ArrayList<Major> M = new ArrayList<Major>();
//ArrayList<Minor> m = new ArrayList<Minor>();
//ArrayList<Course> C = new ArrayList<Course>();
//ArrayList<Schedule> s = new ArrayList<Schedule>();
//Student Ben = new Student(1234,"Ben",JUNIOR,M,m,C,s);
