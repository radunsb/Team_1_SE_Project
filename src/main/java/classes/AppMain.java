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

        // Create the student user
        ArrayList<Major> majors = new ArrayList<>();
        ArrayList<Minor> minors = new ArrayList<>();
        ArrayList<Course> courseHistory = new ArrayList<>();
        ArrayList<Schedule> schedules = new ArrayList<>();

        Search s = new Search("", Main.getCourseCatalog(),null);
        ArrayList<Course> results = s.search("comp");
        Course c1 = results.get(0);
        Course c2 = results.get(1);
        Course c3 = results.get(5);
        Course c4 = results.get(9);
        System.out.println(c1.getCourseCode());
        System.out.println(c2.getCourseCode());
        System.out.println(c3.getCourseCode());
        System.out.println(c4.getCourseCode());

        Schedule defaultSchedule = new Schedule(0, "FALL", 2020, "mySchedule");
        Schedule defaultSchedule2 = new Schedule(-1, "FALL", 2020, "otherSchedule");
        defaultSchedule.addCourse(c1);
        defaultSchedule2.addCourse(c2);
        defaultSchedule.addCourse(c3);
        defaultSchedule2.addCourse(c4);

        schedules.add(defaultSchedule);
        schedules.add(defaultSchedule2);

        currentSchedule = 0;
        nextId = 1;
        user = new Student(9999, "newStudent", Student.Class.OTHER, majors, minors, courseHistory, schedules);


        //Set up Javalin api on port 7979
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(7979);

        //TODO:add api endpoints to actually get data and interact with the backend

        // Search api path to search by name/code -> returns results list in json
        app.get("/search", SearchController::emptySearch);
        app.get("/search/{query}", SearchController::getResults);
        app.get("/search/{query}/{filters}", SearchController::getResultsWithFilters);
        //TODO: add filter endpoints

        app.get("/getScheduleNames", StudentController::getScheduleNames);
        app.get("/getCurrentSchedule", StudentController::getCurrentSchedule);
        app.get("/getSchedule/{scheduleName}", StudentController::getScheduleByName);

        app.get("/createSchedule/{name}", StudentController::createSchedule);
        app.get("/changeScheduleSemester/{semester}", StudentController::changeScheduleSemester);
        app.get("/renameSchedule/{name}", StudentController::renameSchedule);
        app.get("/deleteSchedule/{scheduleName}", StudentController::deleteSchedule);

        app.get("/changeCurrentSchedule/{scheduleName}", StudentController::changeCurrentSchedule);

        app.get("/removeCourse/{courseCode}", StudentController::removeCourse);

        // kind of optional
        //app.post("/changeScheduleName", StudentController::changeScheduleName);




    }
}
