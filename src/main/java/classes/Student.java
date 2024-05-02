package classes;

import classes.Course;
import classes.Major;
import classes.Minor;
import classes.Schedule;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        courseHistory.add(course);
    }

    /**
     * Removes the specified classes.Course object from the courseHistory ArrayList
     * @param course is the course to remove
     */
    public void removeCourseFromHistory(Course course){
        courseHistory.remove(course);
    }

    public void addNewSchedule(int scheduleID, String semester, int year, String scheduleName){
        Schedule s = new Schedule(scheduleID,semester,year, scheduleName);
        schedules.add(s);
    }

    /**
     * A way to load a schedule without actually setting the current schedule to it
     * @param studentID "ID_NAME" of current student (directory of saves)
     * @param childName "ID_NAME" of schedule we're trying to load
     * @return the schedule found by the load
     */
    public Schedule loadSchedule(String studentID, String childName) {
        Scanner inScan;
        int scheduleID;
        String semester;
        String year;
        String scheduleName;
        ArrayList<String> parts = new ArrayList<>();
        try {
            File csvToParse = new File(studentID + "/" + childName);
            inScan = new Scanner(csvToParse);
        } catch (FileNotFoundException fe) {
            System.out.println("The specified schedule does not exist on the system");
            return null;
        }
        inScan.useDelimiter(",");
        while (inScan.hasNext()) {
            parts.add(inScan.next());
        }
        for(String part : parts){
            System.out.println(part);
        }
        if(parts.size() < 3){
            System.out.println("The specified schedule is in an unsupported format");
            return null;
        }
        Main main = new Main();
        ArrayList<Filter> filt = new ArrayList<>();
        Search search = new Search("", main.getCourseCatalog(), filt);
        scheduleID = Integer.parseInt(parts.get(0));
        semester = parts.get(1);
        year = parts.get(2);
        scheduleName = parts.get(3);
        ArrayList<String> filterInput = new ArrayList<>();
        filterInput.add(semester);
        filterInput.add(year);
        Filter semFilter = new Filter(filterInput, Filter.FilterType.SEMESTER);
        filt.add(semFilter);
        List<String> coursePrimaryKeys = parts.subList(4, parts.size());
        List<Course> courses = new ArrayList<>(coursePrimaryKeys.stream().map(course -> {
            search.search(course);
            search.search(filt);
            if (search.getResults().isEmpty()) {
                return null;
            }
            return search.getResults().get(0);
        }).toList());
        Schedule sched = new Schedule(scheduleID,semester,Integer.parseInt(year),scheduleName);
        sched.setCourses(new ArrayList<>(courses));
        inScan.close();
        return sched;
    }

    public ArrayList<Schedule> loadAllSchedules(String studentID){
        ArrayList<Schedule> toReturn = new ArrayList<>();
        File userFolder = new File(studentID);
        File[] scheduleList = userFolder.listFiles();
        if(scheduleList != null) {
            for (File sched : scheduleList) {
                Schedule schedule = loadSchedule(studentID, sched.getName());
                toReturn.add(schedule);
            }
        }
        return toReturn;
    }

    public String switchSchedule(int s){
        return schedules.get(s).toString();
    }
    public String switchSchedule(String name){
        int place = 0;
        while(!schedules.get(place).getScheduleName().equals(name)){
            place ++;
        }
        return schedules.get(place).toString();
    }

    public String switchScheduleByCode(String code){
        int place = 0;
        while(!schedules.get(place).getScheduleName().equals(code)){
            place ++;
        }
        return schedules.get(place).toString();
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

    public String toString() {
        return username;
    }

}
