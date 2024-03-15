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
        ArrayList<Major> M = new ArrayList<Major>();
        ArrayList<Minor> m = new ArrayList<Minor>();
        ArrayList<Course> C = new ArrayList<Course>();
        ArrayList<Schedule> s = new ArrayList<Schedule>();
        Student Ben = new Student(1234,"Ben",JUNIOR,M,m,C,s);
    }

    public HashMap<String, Course> getCourseCatalog() {
        return courseCatalog;
    }

}
