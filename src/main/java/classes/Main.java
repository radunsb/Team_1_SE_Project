package classes;

import classes.Course;

import java.util.HashMap;

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
