package classes;
import classes.Course;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.module.FindException;
import java.util.*;
import java.util.ArrayList;

import static classes.Student.Class.JUNIOR;

public class Schedule {
    private int scheduleID;
    private String semester;

    private int year;
    private String scheduleName;
    private ArrayList<Course> courses;


    public Schedule(int scheduleID, String semester, int year, String scheduleName) {
        this.scheduleID = scheduleID;
        this.semester = semester;
        this.year = year;
        this.scheduleName = scheduleName;
        this.courses = new ArrayList<>();
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
    String courseName = course.getName();
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
    public String toString() {

        StringBuilder str = new StringBuilder();
        System.out.println("\t\t\t\t\t\t\t\t\t" + scheduleName);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("\t8:00a\t9:00a\t10:00a\t11:00a\t12:00p\t1:00p\t2:00p\t3:00\t4:00p\t6:30p");
        System.out.print("M: ");
        System.out.println();
        System.out.print("T: ");
        System.out.println();
        System.out.print("W: ");
        System.out.println();
        System.out.print("R: ");
        System.out.println();
        System.out.print("F: ");
        System.out.println();

        return str.toString();
    }
    /**
     * Takes the data within the current schedule, and saves it to a csv file so that
     * it can be reloaded at a later date
     * @param studentID id of current student, is made the name of the outside directory to save in
     * is of the format "ID_Name"
     */
    public void saveSchedule(String studentID){
        //save the current schedule to a csv file
        File studentFile = new File(studentID);
        studentFile.mkdirs();
        File csvOutputFile = new File(studentFile, scheduleID + "_" + scheduleName);
        ArrayList<String> codes = new ArrayList<>();
        for(Course course : courses){
            codes.add(course.getCourseCode());
        }
        String codeString = String.join(",", codes);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)){
            pw.print(scheduleID + "," + semester + "," + year + "," + scheduleName + "," + codeString);
        }
        catch(FileNotFoundException fe){
            System.out.println("The software was unable to to save your schedule");
        }
    }

    /**
     * Takes in a File and sets the current Schedule parameters to those specified
     * by the file
     * @param studentID directory labelled with the current student's ID
     * @param childName name of the specific schedule that is being loaded
     */
    public void loadSchedule(String studentID, String childName) {
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
            return;
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
            return;
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
        List<Course> additionalTimeslots = new ArrayList<>();
        List<Course> courses = new ArrayList<>(coursePrimaryKeys.stream().map(course -> {
            search.search(course);
            search.search(filt);
            if (search.getResults().size() > 1) {
                for (int i = 1; i < search.getResults().size(); i++) {
                    additionalTimeslots.add(search.getResults().get(i));
                }
            } else if (search.getResults().isEmpty()) {
                throw new FindException("The saved schedule contains a Course that could not be found in the database. Schedule was unable to be loaded");
            }
            return search.getResults().get(0);
        }).toList());
        courses.addAll(additionalTimeslots);
        this.scheduleID = scheduleID;
        this.semester = semester;
        this.scheduleName = scheduleName;
        this.courses = new ArrayList<>(courses);
        inScan.close();
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

                    // Check corner cases for times of each
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

    public int getYear(){
        return year;
    }
}




//ArrayList<Major> M = new ArrayList<Major>();
//ArrayList<Minor> m = new ArrayList<Minor>();
//ArrayList<Course> C = new ArrayList<Course>();
//ArrayList<Schedule> s = new ArrayList<Schedule>();
//Student Ben = new Student(1234,"Ben",JUNIOR,M,m,C,s);
