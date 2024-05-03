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
                return;
            }
        }
    }

    public static void createSchedule(Context context) {
        Schedule s = new Schedule(AppMain.nextId, "FALL", 2020, "New Schedule");
        AppMain.user.addSchedule(s);
        AppMain.currentSchedule = s.getScheduleID();
        AppMain.nextId++;
        context.json(s);
    }

    public static void changeScheduleSemester(Context context) {
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();
        for(Schedule s : schedules){
            if(s.getScheduleID() == AppMain.currentSchedule){
                s.setSemester(context.pathParam("semester"));
            }
        }
    }

    public static void renameSchedule(Context context) {
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();
        for(Schedule s : schedules){
            if(s.getScheduleID() == AppMain.currentSchedule){
                s.setScheduleName(context.pathParam("name"));

            }
        }
    }

    public static void deleteSchedule(Context context) {
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();
        for(Schedule s : schedules){
            if(s.getScheduleName().equals(context.pathParam("scheduleName"))){
                schedules.remove(s);
                return;
            }
        }
    }

    public static void removeCourse(Context context) {
        ArrayList<Schedule> schedules = AppMain.user.getSchedules();
        for(Schedule s : schedules) {
            if (s.getScheduleID() == AppMain.currentSchedule) {
                for (Course c : s.getCourses()) {
                    if (c.getCourseCode().equals(context.pathParam("courseCode"))) {
                        s.removeCourse(c);
                    }
                }
            }
        }
    }
}
