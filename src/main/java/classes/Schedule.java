package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.module.FindException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.time.Duration;

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
     * @param c is the course to add to the schedule
     * @return true if the class was successfully added to courses, false if it was not
     */
    public void addCourse(Course c) {
        if (!courseConflict(c)) {
            courses.add(c);
            sortArr(courses);
        }
    }
    /**
     * Sorts through current schedule and checks for conflict with other classes.
     * @param candidate is the course to add to the schedule
     * @return true if there is a conflict with another course, false if there is no conflict
     */
    private boolean courseConflict(Course candidate) {
        // check if the class is already in the users' schedule
        if (courses.contains(candidate)) {
            System.out.println("The class " +candidate.getCourseCode() +" is already in your schedule.");
            return true;
        }
        // Set meeting times for candidate course
        Date candStartTime = candidate.getMeetingTimes()[0][0];
        Date candEndTime = candidate.getMeetingTimes()[0][1];
        // Loop that checks for conflict with users' timeslots and candidate timeslot
        for (Course check : courses) {
            if (Arrays.equals(check.getMeetingDays(), candidate.getMeetingDays())) {
                // Set meeting times for each course to check
                Date checkStartTime = check.getMeetingTimes()[0][0];
                Date checkEndTime = check.getMeetingTimes()[0][1];

                // Check cases for times of each and make sure they don't overlap
                if (candStartTime.equals(checkStartTime)) {
                    System.out.println("The class " + check + " conflicts with your schedule.");
                    return true;
                }
                if (candStartTime.compareTo(checkEndTime) < 0 && candStartTime.compareTo(checkStartTime) > 0 && candEndTime.compareTo(checkEndTime) > 0) {
                    System.out.println("The class " + check + " conflicts with your schedule.");
                    return true;
                }
                if (candEndTime.compareTo(checkStartTime) > 0 && candStartTime.compareTo(checkStartTime) < 0 && candEndTime.compareTo(checkEndTime) < 0) {
                    System.out.println("The class " + check + " conflicts with your schedule.");
                    return true;
                }
            }
        }
        return false;
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

    // helper method that sorts the class day arrays
    private void sortArr(ArrayList<Course> list) {
        Course hold;
        Course earlier;
        for (int i = 0; i <= list.size(); i++) {
            if (list.get(i + 1).getMeetingTimes()[0][0].getTime() < list.get(i).getMeetingTimes()[0][0].getTime()) {
                earlier = list.get(i + 1);
                hold = list.get(i);
                list.set(i, earlier);
                list.set(i + 1, hold);
            }
        }
    }
    // helper method to return duration between the start and end of a class
    private long classDuration(Course e) {
        return e.getMeetingTimes()[1][0].getTime() - e.getMeetingTimes()[0][0].getTime();
    }
    // helper method that returns the time between two classes
    private long durationBetween(Course e, Course y) {
        long eStart = e.getMeetingTimes()[0][0].getTime();
        long yStart = y.getMeetingTimes()[0][0].getTime();
        if (eStart < yStart) {
            return yStart - e.getMeetingTimes()[1][0].getTime();
        } else {
            return eStart - y.getMeetingTimes()[1][0].getTime();
        }
    }

        /**
         * toString method for a schedule.
         * @return a string representation of a schedule in a weekly timeslot format
         */
        public String toString () {
            StringBuilder str = new StringBuilder();
            // Prints the schedule ID
            str.append("Schedule ID: | ");
            str.append(scheduleID);
            str.append(" |\n");
            str.append("M:\t");
            // print classes
            for (Course e: courses) {
                if (e.getMeetingDays()[0]) {
                    str.append(e);
                }
            }
            str.append("\n\nT:\t");
            // print classes
            for (Course e: courses) {
                if (e.getMeetingDays()[1]) {
                    str.append(e);
                }
            }
            str.append("\n\nW:\t");
            // print classes
            for (Course e: courses) {
                if (e.getMeetingDays()[2]) {
                    str.append(e);
                }
            }
            str.append("\n\nR:\t");
            // print classes
            for (Course e: courses) {
                if (e.getMeetingDays()[3]) {
                    str.append(e);
                }
            }
            str.append("\n\nF:\t");
            // print classes
            for (Course e: courses) {
                if (e.getMeetingDays()[4]) {
                    str.append(e);
                }
            }
            return str.toString();
        }


    /**
     * Takes the data within the current schedule, and saves it to a csv file so that
     * it can be reloaded at a later date
     * @param studentID id of current student, is made the name of the outside directory to save in
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
    }


    /**
     * This method will check the candidate course with the users current schedule for conflicts with classes
     *
     * @param candidate possible class to be added to the schedule
     * @return returns the candidate if no conflict is detected, otherwise returns the course that the candidate conflicts with
     */

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
