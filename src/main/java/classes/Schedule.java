package classes;

import classes.Course;

import java.util.ArrayList;
import java.util.Date;

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
        if (courseConflict(course).equals(course)) {
            courses.add(course);
        }
    }

    /**
     * Removes the specified classes.Course object from the courses ArrayList
     * @param course is the course to remove from the schedule
     */
    public void removeCourse(Course course){

    }

    /**
     * toString method for a schedule.
     * @return a string representation of a schedule in a weekly timeslot format
     */
    public String toString(){
        return null;
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

    // private method that will check candidate course with existing courses for conflict
    // returns candidate course if there is no conflict, returns course it conflicts with otheriwse
    private Course courseConflict(Course candidate) {
        Date candidateStartTime = candidate.getMeetingTimes().get(0);
        Date candidateEndTime = candidate.getMeetingTimes().get(0);

        for (Course check: courses) {
            if (check.getCourseCode().equals(candidate.getCourseCode()))
                return check;

            Date existingStartTime = check.getMeetingTimes().get(0);
            Date existingEndTime = check.getMeetingTimes().get(0);
            if (existingEndTime.after(candidateStartTime) || existingStartTime.before(candidateEndTime)) {
                return check;
            }
        }

        return candidate;
    }
}
