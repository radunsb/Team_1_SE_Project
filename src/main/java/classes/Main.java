package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.*;

public class Main {

    private static Schedule currentSchedule;
    private static Student currentStudent;
    private static ArrayList<Course> courseCatalog;

    private static int schedCount;


    public static void main(String[] args) {
        run();
    }

    //hi
    public static void run() {

        Scanner input = new Scanner(System.in);

        System.out.println("Hello please enter 1 to create a new user");
        System.out.println("Or Enter 2 to log in as an existing user");
        int confirm = input.nextInt();
        if (confirm == 1) {
            Student ben = craftUser(input);
            currentStudent = ben;
            schedCount = 0;
            System.out.println("congratulations " + ben.toString() + " welcome to our app");
            navigateHome(input, ben);
        } else if (confirm == 0) {
            System.out.println("This will be where you log in as an existing user");
        }

    }

    /**
     * Prompts user to enter in account information to create a new student
     */
    public static Student craftUser(Scanner s) {
        //ask for ID
        System.out.println("please enter your student ID");
        int id = s.nextInt();

        //ask for username
        System.out.println("please enter a username");
        String username = s.next();

        ArrayList<Major> majors = new ArrayList<>();

        //give no one a minor
        ArrayList<Minor> minors = new ArrayList<>();

        //no course history right now
        ArrayList<Course> courseHistory = new ArrayList<>();

        //add empty schedule
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Student newStudent = new Student(id, username, Student.Class.JUNIOR, majors, minors, courseHistory, schedules);

        currentStudent = newStudent;

        return newStudent;
    }

    public static void navigateHome(Scanner s, Student current) {
        int state;
        String command = "";

        if (current.getSchedules().isEmpty()) {
            int year = Year.now().getValue();
            current.addNewSchedule(1, "Spring", year, "defaultSchedule");
            System.out.println("You did not have any schedules.\nHere is a default schedule\n");
            currentSchedule = current.getSchedules().getFirst();
        }


        while (true) {
            System.out.println("You are currently editing: " + currentSchedule.getScheduleName());
            System.out.println();
            System.out.println("Enter 1 to make a new schedule");
            System.out.println("Enter 2 to view current Schedule");
            System.out.println("Enter 3 to search courses");
            System.out.println("Enter 4 to edit Schedules");
            System.out.println("\nEnter EXIT to log out");
            command = s.next();

            if (!command.equals("EXIT")) {
                state = Integer.parseInt(command);
            } else {
                return;
            }

            if (state == 1) {
                System.out.println("What would you like to call this schedule?");
                String newScheduleName = s.next();
                System.out.println("What Semester is this schedule for");
                String newScheduleSemester = s.next();
                System.out.println("What year is this schedule for?");
                int year = s.nextInt();
                current.addNewSchedule(schedCount, newScheduleSemester, year, newScheduleName);
                currentSchedule = current.getSchedules().getLast();
                System.out.println(currentSchedule.getScheduleName());
                schedCount++;
            }

            if (state == 2) {
                System.out.println(currentSchedule.toString());
            }
            if (state == 3) {
                //do search method here
                //searchCourses();
            }

            if (state == 4) {
                navigateSchedules(s, current);
            }
        }

    }

    public static void navigateSchedules(Scanner s, Student current) {
        int state = 0;
        int temp;

        while (state != 5) {
            if (state == 0) {
                System.out.println("You are currently editing: " + currentSchedule.getScheduleName());
                System.out.println();
            }

            System.out.println("Enter 1 to add a course to " + currentSchedule.getScheduleName());
            System.out.println("Enter 2 to remove a course from " + currentSchedule.getScheduleName());
            System.out.println("Enter 3 to rename this schedule");
            System.out.println("Enter 4 to switch schedule");
            System.out.println("Enter 5 to return to home");
            state = s.nextInt();

            if (state == 1) {
                // go to search method

                // add searched class to schedule

            } else if (state == 2) {

                // print out schedule in a list format
                for (int i = 0; i < currentSchedule.getCourses().size(); i++) {
                    System.out.println(i + 1 + ".)" + currentSchedule.getCourses().get(i).toString());
                }
                System.out.println("Enter the number of course you would like to remove from your schedule");
                currentSchedule.getCourses().remove(s.nextInt() - 1);


            } else if (state == 3) {
                System.out.println("This schedules current name is " + currentSchedule.getScheduleName());
                System.out.println("Enter what you would like to name this schedule");
                String newName = s.next();
                currentSchedule.setScheduleName(newName);

            } else if (state == 4) {
                for (int i = 0; i < current.getSchedules().size(); i++) {
                    System.out.print(i + 1 + ".) ");
                    System.out.println(current.getSchedules().get(i).toString());
                }

                System.out.println("\nEnter the number of the schedule you would like to edit");
                temp = s.nextInt() - 1;


                if (temp > current.getSchedules().size()) {
                    currentSchedule = current.getSchedules().getLast();
                } else if (temp < 1) {
                    currentSchedule = current.getSchedules().getFirst();
                } else {
                    currentSchedule = current.getSchedules().get(temp);
                }
            } else {
                state = 5;
            }
        }

    }


    // Read the CSV
//        try{
//            readCSV();
//        } catch(FileNotFoundException | ParseException e){
//            System.out.println(e.getMessage());
//        }


    // Sanity Testing: I tried it with pretty much all the attributes, and they all seem good
    // Since its kind of hard to unit test it lol
        /*
        for(Course c: courseCatalog){
            if(c.getMeetingTimes() != null) {
                System.out.println(c.getMeetingTimes()[0][0]);
            }
        }
        */

    //}

    /**
     * Reads the 2020-2021.csv file and parses the data into an ArrayList of course objects
     *
     * @throws FileNotFoundException if the file isn't found
     * @throws ParseException        if something weird happens (it shouldn't, hopefully. . .)
     */
    private static void readCSV() throws FileNotFoundException, ParseException {
        courseCatalog = new ArrayList<>();

        Scanner scnr = new Scanner(new File("2020-2021.csv"));
        // Get the header line and skip it
        String headLine = scnr.nextLine();

        while (scnr.hasNext()) {
            Scanner line = new Scanner(scnr.nextLine());
            line.useDelimiter(",");

            int year = line.nextInt();
            int term = line.nextInt();
            Course.Semester semester;
            if (term == 10) {
                semester = Course.Semester.FALL;
            } else {
                semester = Course.Semester.SPRING;
            }
            StringBuilder code = new StringBuilder();
            code.append(line.next());
            code.append(line.next());
            code.append(line.next());
            String courseName = line.next();
            int credits = line.nextInt();
            int capacity = line.nextInt();
            // NOT USING ENROLLMENT
            int enrollment = line.nextInt();
            boolean[] days = new boolean[5];
            for (int i = 0; i < 5; i++) {
                String day = line.next();
                if (day.isEmpty()) {
                    days[i] = false;
                } else {
                    days[i] = true;
                }
            }

            String startTime = line.next();
            String endTime = line.next();

            Date[][] times = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");

            // Some of the classes don't have times (Online courses)
            if (!startTime.equals("")) {
                times = new Date[2][5];
                for (int i = 0; i < days.length; i++) {
                    if (days[i]) {
                        times[0][i] = dateFormat.parse(startTime);
                        times[1][i] = dateFormat.parse(endTime);
                    } else {
                        times[0][i] = null;
                        times[1][i] = null;
                    }
                }
            }

            String profLast = line.next();
            String profFirst = line.next();

            // TODO: If we want the comments off the csv file, they need to be dealt with here
            // TODO: Also need to write the logic for combining weird courses such as calculus -> will do later

            Course newCourse = new Course(code.toString(),
                    courseName,
                    "Not Available",
                    profFirst + " " + profLast,
                    times,
                    days,
                    year,
                    credits,
                    semester,
                    capacity,
                    null,
                    null,
                    null
            );

            courseCatalog.add(newCourse);

        }

    }

    public ArrayList<Course> getCourseCatalog() {
        return courseCatalog;
    }

}
