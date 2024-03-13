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
        //TODO: read CSV here
        //Pull test comment
    }

    public HashMap<String, Course> getCourseCatalog() {
        return courseCatalog;
    }

    public void setCourseCatalog(HashMap<String, Course> map){
        this.courseCatalog = map;
    }

}
