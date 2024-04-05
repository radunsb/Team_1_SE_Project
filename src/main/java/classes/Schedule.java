package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.module.FindException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;

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
    public boolean addCourse(Course c) {
        if (!courseConflict(c)) {
            courses.add(c);
            //sortArr(courses);
            return true;
        }
        return false;
    }

    /**
     * Sorts through current schedule and checks for conflict with other classes.
     * @param candidate is the course to add to the schedule
     * @return true if there is a conflict with another course, false if there is no conflict
     */
    private boolean courseConflict(Course candidate) {
        // check if the class is already in the users' schedule
        for(Course c : courses){
            if(c.getCourseCode().equals(candidate.getCourseCode())){
                return true;
            }
        }
        // check if the candidate class has no meeting times
        if(candidate.getMeetingTimes() == null){
            return false;
        }
        // Search Object to use the isTimeBetween method
        Search s = new Search("",null, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        for(Course c : courses){
            for(int i = 0; i < 5; i++){
                if(c.getMeetingTimes()[i][0] != null && candidate.getMeetingTimes()[i][0] != null){
                    String start = dateFormat.format(c.getMeetingTimes()[i][0]);
                    String end = dateFormat.format(c.getMeetingTimes()[i][1]);
                    if(s.isTimeBetween(candidate.getMeetingTimes()[i][0], start, end) || s.isTimeBetween(candidate.getMeetingTimes()[i][1], start, end)){
                        return true;
                    }
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

    public String toStringEx(){
        StringBuilder str = new StringBuilder();
        str.append("Schedule ID: | ");
        str.append(scheduleID);
        str.append(" |\n");

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String[] endTimes = {"8:59 AM", "9:59 AM","10:59 AM","11:59 AM","12:59 PM","1:59 PM","2:59 PM","3:59 PM","4:59 PM","5:59 PM","6:59 PM","7:59 PM"};
        String[] times = {"8:00 AM", "9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM","6:00 PM","7:00 PM"};
        String[] days = {"M", "T", "W", "R", "F"};
        HashMap<String, Integer> dayIndexes = new HashMap<>();
        dayIndexes.put("M", 0);
        dayIndexes.put("T", 1);
        dayIndexes.put("W", 2);
        dayIndexes.put("R", 3);
        dayIndexes.put("F", 4);
        Search s = new Search("", null, null);

        str.append("                 8:00a                            9:00a                            10:00a                            11:00a                            12:00p                            1:00p                            2:00p                            3:00p                            4:00                            5:00p                            6:00p                            7:00p              \n");
        for(String day : days){
            str.append(day);
            str.append(":   ");
            for(int i = 0; i < times.length; i++){
                boolean timeFull = false;
                for(Course c : courses){
                    if(c.getMeetingDays()[dayIndexes.get(day)] && (day.equals("T") || day.equals("R")) && s.isTimeBetween(c.getMeetingTimes()[dayIndexes.get(day)][0], times[i], endTimes[i]) && i != 0){
                        str.append("   ");
                        str.append(c);
                        str.append("     ");
                        timeFull = true;
                    }else if(c.getMeetingDays()[dayIndexes.get(day)] && s.isTimeBetween(c.getMeetingTimes()[dayIndexes.get(day)][0], times[i], endTimes[i])){
                        str.append(c);
                        str.append("       ");
                        timeFull = true;
                    }
                }
                if(!timeFull){
                    str.append("                                ");
                }
            }
            str.append("\n");
        }
        str.append("Online/other courses: ");
        for(Course c : courses){
            if(c.getMeetingTimes() == null){
                str.append(c);
                str.append(", ");
            }
        }
        return str.toString();
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

            str.append("\t8:00a\t\t\t\t\t\t9:00a\t\t\t\t\t\t10:00a\t\t\t\t\t\t11:00a\t\t\t\t\t\t12:00p\t\t\t\t\t\t1:00p\t\t\t\t\t\t2:00p\t\t\t\t\t\t3:00p\t\t\t\t\t\t4:00\t\t\t\t\t\t6:30p");
            str.append("\nM:\t");

            // print classes
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String startTime = dateFormat.format(courses.getFirst().getMeetingTimes()[0][0]);


            for (Course e : courses) {
                str.append(e);
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
            str.append("\n\nOnline\t");
            for (Course e: courses){
                if(!areAllTrue(e.getMeetingDays())){
                    str.append(e);
                }
            }
            return str.toString();
        }


    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
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
