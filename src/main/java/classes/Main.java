package classes;

import classes.Course;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private static Schedule currentSchedule;
    private static HashMap<String, Course> courseCatalog; //course codes -> courses


    public static void main(String[] args) {
        run();
    }

    public static void run(){
        //TODO: read CSV here
        //Pull test comment
        try{
            readCSV();
        } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }

        Course c1 = courseCatalog.get("ACCT201A");
        System.out.println(c1.getName());
    }

    private static void readCSV() throws FileNotFoundException {
        courseCatalog = new HashMap<String, Course>();

        Scanner scnr = new Scanner(new File("2020-2021.csv"));
        scnr.useDelimiter(",");
        // Get the header line and skip it
        String headLine = scnr.nextLine();

        while(scnr.hasNext()){
            Scanner line = new Scanner(scnr.nextLine());
            line.useDelimiter(",");
            int year = line.nextInt();
            int term = line.nextInt();
            StringBuilder code = new StringBuilder();
            code.append(line.next());
            code.append(line.next());
            code.append(line.next());
            String courseName = line.next();
            int credits = line.nextInt();
            int capacity = line.nextInt();
            // NOT USING ENROLLMENT
            int enrollment = line.nextInt();
            char[] days = new char[5];
            for(int i = 0; i < 5; i++){
                // Some are empty
                days[i] = line.next().charAt(0);
            }
            ArrayList<String> times = new ArrayList<>();
            times.add(line.next());
            times.add(line.next());
            String profLast = line.next();
            String profFirst = line.next();
            String preferred = line.next();
            String otherInfo = line.next();

            Course newCourse = new Course(code.toString(),
                    courseName,
                    "Not Available",
                    profFirst+" "+profLast,
                    null,
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
