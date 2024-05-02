package classes;

import io.javalin.http.Context;
import org.eclipse.jetty.webapp.MetaInfConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentController {
    public static void getSchedules(Context context) {
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();
        context.json(schedules);
    }

    public static void getScheduleNames(Context context) {
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();

        ArrayList<String> scheduleNames = new ArrayList<>();
        for(Schedule s : schedules) {
            scheduleNames.add(s.getScheduleName());
        }

        context.json(scheduleNames);
    }

    public static void getScheduleByName(Context context) {
        String scheduleName = context.pathParam("scheduleName");
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();
        for(Schedule s : schedules){
            if(s.getScheduleName().equals(scheduleName)){
                context.json(s);
                return;
            }
        }
    }

    public static void getCurrentSchedule(Context context) {
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();
        for(Schedule s : schedules){
            if(s.getScheduleID() == AppMain.currentSchedule){
                context.json(s);
                return;
            }
        }
    }

    public static void changeCurrentSchedule(Context context) {
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();
        for(Schedule s : schedules) {
            if (s.getScheduleName().equals(context.pathParam("scheduleName"))) {
                AppMain.currentSchedule = s.getScheduleID();
            }
        }
    }
}
