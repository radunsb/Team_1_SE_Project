package classes;

import classes.Course;
import classes.Schedule;
import classes.Student;
import classes.Major;
import java.util.ArrayList;
import java.util.HashMap;

import static classes.Student.Class.JUNIOR;

public class Main {

    private Schedule currentSchedule;
    private HashMap<String, Course> courseCatalog; //course codes -> courses


    public static void main(String[] args) {
        run();
    }

    public static void run(){


    }

    public HashMap<String, Course> getCourseCatalog() {
        return courseCatalog;
    }

}
