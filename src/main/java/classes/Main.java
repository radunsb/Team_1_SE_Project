package classes;

import classes.Course;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

public class Main {

    private static Schedule currentSchedule;
    private static HashMap<String, Course> courseCatalog; //course codes -> courses


    public static void main(String[] args) {
        run();
    }

    public static void run(){
        // Read the CSV
        try{
            readCSV();
        } catch(FileNotFoundException | ParseException e){
            System.out.println(e.getMessage());
        }

//        Course c1 = courseCatalog.get("ACCT201A");
//        System.out.println(c1.getName());
//        System.out.println(c1.getMeetingTimes()[0][0]);

        for(Course c : courseCatalog.values()){
            System.out.println(c.getProfessor());
        }
    }

    private static void readCSV() throws FileNotFoundException, ParseException {
        courseCatalog = new HashMap<String, Course>();

        Scanner scnr = new Scanner(new File("2020-2021.csv"));
        // Get the header line and skip it
        String headLine = scnr.nextLine();

        while(scnr.hasNext()){
            Scanner line = new Scanner(scnr.nextLine());
            line.useDelimiter(",");

            int year = line.nextInt();
            int semester = line.nextInt();
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

            // TODO: Parse and format the times properly; Talk to Jackson to figure out format
            String startTime = line.next();
            String endTime = line.next();

            Scanner sc1 = new Scanner(startTime);
            sc1.useDelimiter(":");
            int sHrs = sc1.nextInt();
            int sMin = sc1.nextInt();

            Scanner sc2 = new Scanner(endTime);
            sc2.useDelimiter(":");
            int eHrs = sc2.nextInt();
            int eMin = sc2.nextInt();

            Date[][] times = new Date[2][5];

            for(int i = 0; i < days.length; i++) {
                if (days[i]) {
                    times[0][i] = new Date(1970, Calendar.JANUARY, 1, sHrs, sMin);
                    times[1][i] = new Date(1970, Calendar.JANUARY, 1, eHrs, eMin);
                }else{
                    times[0][i] = null;
                    times[1][i] = null;
                }
            }

            String profLast = line.next();
            String profFirst = line.next();

            // If we want the comments off the csv file, they need to be dealt with here

            Course newCourse = new Course(code.toString(),
                    courseName,
                    "Not Available",
                    profFirst+" "+profLast,
                    times,
                    days,
                    null,
                    null,
                    null
                    );

            courseCatalog.put(code.toString(), newCourse);

        }

    }

    public HashMap<String, Course> getCourseCatalog() {
        return courseCatalog;
    }

}
