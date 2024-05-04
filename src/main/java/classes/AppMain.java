package classes;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class AppMain {

    public static Student user;
    public static int currentSchedule;
    public static int nextId;

    public static void main(String[] args) {
        //Read the CSV
        try{
            Main.readCSV();
        } catch(FileNotFoundException | ParseException e){
            System.out.println(e.getMessage());
        }

        // Setup for a new Student user
        // Create the student user attributes
        ArrayList<Major> majors = new ArrayList<>();
        ArrayList<Minor> minors = new ArrayList<>();
        ArrayList<Course> courseHistory = new ArrayList<>();
        ArrayList<Schedule> schedules = new ArrayList<>();
        // Add a default schedule to the student
        Schedule defaultSchedule = new Schedule(0, "FALL", 2020, "My First Schedule");
        schedules.add(defaultSchedule);
        // Setup variables
        currentSchedule = 0;
        nextId = 1;
        // Create the user
        user = new Student(9999, "newStudent", Student.Class.OTHER, majors, minors, courseHistory, schedules);

        //Set up Javalin api on port 7979
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(7979);

        // Search api endpoints to search by name/code and add filters
        app.get("/search", SearchController::emptySearch);
        app.get("/search/{query}", SearchController::getResults);
        app.get("/search/{query}/{filters}", SearchController::getResultsWithFilters);

        // Api endpoints to get the schedule variables
        app.get("/getScheduleNames", StudentController::getScheduleNames);
        app.get("/getCurrentSchedule", StudentController::getCurrentSchedule);
        app.get("/getSchedule/{scheduleName}", StudentController::getScheduleByName);

        // Api endpoints to modify schedules
        app.get("/createSchedule/{name}", StudentController::createSchedule);
        app.get("/changeScheduleSemester/{semester}", StudentController::changeScheduleSemester);
        app.get("/renameSchedule/{name}", StudentController::renameSchedule);
        app.get("/addCourseToSchedule/{course}", StudentController::addCourseToSchedule);

        // Delete schedules
        app.get("/deleteSchedule/{scheduleName}", StudentController::deleteSchedule);

        // Switch which schedule to edit
        app.get("/changeCurrentSchedule/{scheduleName}", StudentController::changeCurrentSchedule);

        // Remove course from schedule
        app.get("/removeCourse/{courseCode}", StudentController::removeCourse);

    }
}
