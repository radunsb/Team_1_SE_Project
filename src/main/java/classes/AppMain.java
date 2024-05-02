package classes;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class AppMain {

    public static Student user;
    public static int currentSchedule;

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
        Schedule defaultSchedule = new Schedule(0, "FALL", 2020, "mySchedule");
        Schedule defaultSchedule2 = new Schedule(1, "FALL", 2020, "otherSchedule");
        schedules.add(defaultSchedule);
        schedules.add(defaultSchedule2);

        currentSchedule = 0;
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
        //TODO: add filter endpoints

        app.get("/getScheduleNames", StudentController::getScheduleNames);
        app.get("/getCurrentSchedule", StudentController::getCurrentSchedule);
        app.get("/getSchedule/{scheduleName}", StudentController::getScheduleByName);

        app.get("/changeCurrentSchedule/{scheduleName}", StudentController::changeCurrentSchedule);

        // kind of optional
        //app.post("/changeScheduleName", StudentController::changeScheduleName);




    }
}
