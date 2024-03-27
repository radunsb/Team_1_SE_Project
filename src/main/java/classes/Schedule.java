package classes;
import classes.Course;
import java.util.*;
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
        Course check = courseConflict(course);
        if (check.equals(course)) {
            courses.add(course);
        }
        else {
            System.out.println("The following course conflicts with your candidate course: " +check.toString());
        }
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

    /**
     * This method will check the candidate course with the users current schedule for conflicts with classes
     *
     * @param candidate possible class to be added to the schedule
     * @return returns the candidate if no conflict is detected, otherwise returns the course that the candidate conflicts with
     */
    private Course courseConflict(Course candidate) {
        // Loop to check if the class is already in the users' schedule
        for (Course check : courses) {
            if (check.getName().equals(candidate.getName())) {
                return check;
            }
        }
        // Loop that checks for conflict with users' timeslots and candidate timeslot
        // Set times for candidate
        for (Course check : courses) {
            if (candidate.getMeetingDays() == check.getMeetingDays()) {
                for (int i = 0; i < candidate.getMeetingTimes().length; i++) {
                    // Set meeting times for candidate course
                    long candStartTime = candidate.getMeetingTimes()[0][i].getTime();
                    long candEndTime = candidate.getMeetingTimes()[1][i].getTime();
                    // Set meeting times for each course to check
                    long checkStartTime = check.getMeetingTimes()[0][i].getTime();
                    long checkEndTime = check.getMeetingTimes()[1][i].getTime();

                    // Check cases for times of each and make sure they don't overlap
                    if (candStartTime < checkEndTime && candStartTime > checkStartTime && candEndTime > checkEndTime) {
                        return check;
                    } else if (candEndTime > checkStartTime && candStartTime < checkStartTime && candEndTime < checkEndTime) {
                        return check;
                    }
                }
            }
        }
        return candidate;
    }
}
