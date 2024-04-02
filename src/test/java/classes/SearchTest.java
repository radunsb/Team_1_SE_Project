package classes;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    @Test
    void searchDayOneIn() {
        Date[][] emptyTimes = {{}, {}, {}, {}, {}};
        ArrayList<String> daysWanted = new ArrayList<>();
        daysWanted.add("M");
        boolean[] days = {true, false, false, false, false};
        ArrayList<String> emptyList = new ArrayList<>();
        Course testCourse1 = new Course("1", "test1", "test course",
                "James Borg", emptyTimes, days, 0, 0, Course.Semester.FALL, 30, emptyList, emptyList, emptyList);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(testCourse1);
        Filter monday = new Filter(daysWanted, Filter.FilterType.DAY);
        ArrayList<Filter> filters = new ArrayList<>();
        filters.add(monday);
        Search newSearch = new Search("", courses, filters);
        ArrayList<Course> endCourses = newSearch.search(filters);
        assertEquals(1, endCourses.size());
    }
    @Test
    void searchDayOneOut() {
        Date[][] emptyTimes = {{}, {}, {}, {}, {}};
        ArrayList<String> daysWanted = new ArrayList<>();
        daysWanted.add("T");
        daysWanted.add("R");
        boolean[] days = {true, false, true, false, true};
        ArrayList<String> emptyList = new ArrayList<>();
        Course testCourse1 = new Course("1", "test1", "test course",
                "James Borg", emptyTimes, days, 0, 0, Course.Semester.FALL, 30, emptyList, emptyList, emptyList);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(testCourse1);
        Filter tr = new Filter(daysWanted, Filter.FilterType.DAY);
        ArrayList<Filter> filters = new ArrayList<>();
        filters.add(tr);
        Search newSearch = new Search("", courses, filters);
        ArrayList<Course> endCourses = newSearch.search(filters);
        assertEquals(0, endCourses.size());
    }

    @Test
    void searchDayOneInOneOut(){
        Date[][] emptyTimes = {{}, {}, {}, {}, {}};
        ArrayList<String> daysWanted = new ArrayList<>();
        daysWanted.add("M");
        boolean[] days1 = {true, false, true, false, true};
        boolean[] days2 = {false, true, false, true, false};
        ArrayList<String> emptyList = new ArrayList<>();
        Course testCourse1 = new Course("1", "test1", "test course",
                "James Borg", emptyTimes, days1, 0, 0, Course.Semester.FALL, 30, emptyList, emptyList, emptyList);
        Course testCourse2 = new Course("1", "test1", "test course",
                "James Borg", emptyTimes, days2, 0, 0, Course.Semester.FALL, 30, emptyList, emptyList, emptyList);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(testCourse1);
        courses.add(testCourse2);
        Filter monday = new Filter(daysWanted, Filter.FilterType.DAY);
        ArrayList<Filter> filters = new ArrayList<>();
        filters.add(monday);
        Search newSearch = new Search("", courses, filters);
        ArrayList<Course> endCourses = newSearch.search(filters);
        assertEquals(1, endCourses.size());
    }

    @Test
    void searchTimeIn() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("10:00:00 AM");
        Date endTime = dateFormat.parse("10:50:00 AM");
        ArrayList<String> userTimes = new ArrayList<String>();
        userTimes.add("10:00 AM");
        userTimes.add("12:50 PM");
        Date[][] dates = {{startTime, endTime},{startTime, endTime},{startTime, endTime},{startTime, endTime},{startTime, endTime}};
        boolean[] days = {true, true, true, true, true};
        ArrayList<String> emptyList = new ArrayList<>();
        Course testCourse1 = new Course("1", "test1", "test course",
                "James Borg", dates, days, 0, 0, Course.Semester.FALL, 30, emptyList, emptyList, emptyList);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(testCourse1);
        Filter filt = new Filter(userTimes, Filter.FilterType.TIME);
        ArrayList<Filter> filters = new ArrayList<>();
        filters.add(filt);
        Search newSearch = new Search("", courses, filters);
        ArrayList<Course> endCourses = newSearch.search(filters);
        assertEquals(1, endCourses.size());
    }

    @Test
    void searchTimeOut() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("12:30:00 PM");
        Date endTime = dateFormat.parse("01:45:00 PM");
        ArrayList<String> userTimes = new ArrayList<String>();
        userTimes.add("01:00 PM");
        userTimes.add("02:50 PM");
        Date[][] dates = {{startTime, endTime},{startTime, endTime},{startTime, endTime},{startTime, endTime},{startTime, endTime}};
        boolean[] days = {true, true, true, true, true};
        ArrayList<String> emptyList = new ArrayList<>();
        Course testCourse1 = new Course("1", "test1", "test course",
                "James Borg", dates, days, 0, 0, Course.Semester.FALL, 30, emptyList, emptyList, emptyList);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(testCourse1);
        Filter filt = new Filter(userTimes, Filter.FilterType.TIME);
        ArrayList<Filter> filters = new ArrayList<>();
        filters.add(filt);
        Search newSearch = new Search("", courses, filters);
        ArrayList<Course> endCourses = newSearch.search(filters);
        assertEquals(0, endCourses.size());
    }

}