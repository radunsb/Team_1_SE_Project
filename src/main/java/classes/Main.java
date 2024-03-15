package classes;

import classes.Course;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

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
        } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }

//        Course c1 = courseCatalog.get("ACCT201A");
//        System.out.println(c1.getName());

//        for(Course c : courseCatalog.values()){
//            System.out.println(c.getProfessor());
//        }
    }

    private static void readCSV() throws FileNotFoundException {
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

            Date[][] times = new Date[2][5];



            String profLast = line.next();
            String profFirst = line.next();

            // If we want the comments off the csv file, they need to be dealt with here

            Course newCourse = new Course(code.toString(),
                    courseName,
                    "Not Available",
                    profFirst+" "+profLast,
                    null,
                    null,
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
