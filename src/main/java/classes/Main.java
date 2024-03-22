package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;

public class Main {

    private static ArrayList<Object> userValues;
    private static Schedule currentSchedule;
    private static ArrayList<Course> courseCatalog;


    public static void main(String[] args) {
        run();
    }

    public static void run() {

        Scanner input = new Scanner(System.in);

        System.out.println("Hello please enter 1 to create a new user");
        int confirm = input.nextInt();
        if (confirm == 1) {
            userValues = new ArrayList<>();
            System.out.println("alright lets make a new user");
            Student ben = craftUser(input);
            System.out.println("congratulations " + ben.toString() + " welcome to our app");

        } else {
            System.out.println("why would you not want to create a user");
        }

    }

    /**
     * Prompts user to enter in account information and arranges info into
     * an arraylist so a new student can be created
     */
    public static Student craftUser(Scanner s){
        //ask for ID
        System.out.println("please enter your student ID");
        int id = s.nextInt();
        //userValues.add(s.nextInt());

        //ask for username
        System.out.println("please enter a username");
        String username = s.next();
        //userValues.add(s.next());

        //make everyone a Junior for right now
        //userValues.add(Student.Class.JUNIOR);

        //make everyone a compsci major
        ArrayList<Major> majors = new ArrayList<>();
        ArrayList<String> majorRequirments = new ArrayList<>();
        Major compSci = new Major(1,"Computer Science", majorRequirments);

        //give no one a minor
        ArrayList<Minor> minors = new ArrayList<>();
        ArrayList<String> minorRequirments = new ArrayList<>();
        Minor n = new Minor(1,"nothing",minorRequirments);

        //no course history right now
        ArrayList<Course> courseHistory = new ArrayList<>();

        //add empty schedule
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Student newStudent = new Student(id,username,Student.Class.JUNIOR,majors,minors,courseHistory,schedules);
        System.out.println(newStudent.getSchedules());
        return newStudent;
    }


        // Read the CSV
//        try{
//            readCSV();
//        } catch(FileNotFoundException | ParseException e){
//            System.out.println(e.getMessage());
//        }



        // Sanity Testing: I tried it with pretty much all the attributes, and they all seem good
        // Since its kind of hard to unit test it lol
//        for(Course c: courseCatalog){
//            System.out.println(c.getCourseCode());
//        }

    //}

    /**
     * Reads the 2020-2021.csv file and parses the data into an ArrayList of course objects
     * @throws FileNotFoundException if the file isn't found
     * @throws ParseException if something weird happens (it shouldn't, hopefully. . .)
     */
    private static void readCSV() throws FileNotFoundException, ParseException {
        courseCatalog = new ArrayList<>();

        Scanner scnr = new Scanner(new File("2020-2021.csv"));
        // Get the header line and skip it
        String headLine = scnr.nextLine();

        while(scnr.hasNext()){
            Scanner line = new Scanner(scnr.nextLine());
            line.useDelimiter(",");

            int year = line.nextInt();
            int term = line.nextInt();
            Course.Semester semester;
            if(term == 10){
                semester = Course.Semester.FALL;
            }else{
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
            for(int i = 0; i < 5; i++){
                String day = line.next();
                if(day.isEmpty()){
                    days[i] = false;
                }
                else{
                    days[i] = true;
                }
            }

            String startTime = line.next();
            String endTime = line.next();

            Date[][] times = null;

            // Some of the classes don't have times (Online courses)
            if(!startTime.equals("")){
                Scanner sc1 = new Scanner(startTime);
                sc1.useDelimiter(":");
                int sHrs = sc1.nextInt();
                int sMin = sc1.nextInt();

                Scanner sc2 = new Scanner(endTime);
                sc2.useDelimiter(":");
                int eHrs = sc2.nextInt();
                int eMin = sc2.nextInt();

                times = new Date[2][5];
                for(int i = 0; i < days.length; i++) {
                    if (days[i]) {
                        times[0][i] = new Date(1970, Calendar.JANUARY, 1, sHrs, sMin);
                        times[1][i] = new Date(1970, Calendar.JANUARY, 1, eHrs, eMin);
                    }else{
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
                    profFirst+" "+profLast,
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
