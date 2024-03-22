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
    void timeBetweenBothAMIn() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("08:00:00 AM");
        Date endTime = dateFormat.parse("11:00:00 AM");
        String userInput = "10:30 AM";
        Search newSearch = new Search("", new ArrayList<Course>(), new ArrayList<Filter>());
        boolean result = newSearch.isTimeBetween(userInput, startTime, endTime);
        assertTrue(result);
    }

    @Test
    void timeBetweenBothPMIn() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("02:15:00 PM");
        Date endTime = dateFormat.parse("04:30:00 PM");
        String userInput = "03:20 PM";
        Search newSearch = new Search("", new ArrayList<Course>(), new ArrayList<Filter>());
        boolean result = newSearch.isTimeBetween(userInput, startTime, endTime);
        assertTrue(result);
    }

    @Test
    void timeBetweenBothAMOut() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("08:00:00 AM");
        Date endTime = dateFormat.parse("11:00:00 AM");
        String userInput = "11:30 AM";
        Search newSearch = new Search("", new ArrayList<Course>(), new ArrayList<Filter>());
        boolean result = newSearch.isTimeBetween(userInput, startTime, endTime);
        assertFalse(result);
    }

    @Test
    void timeBetweenBothPMOut() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("01:00:00 PM");
        Date endTime = dateFormat.parse("04:20:00 PM");
        String userInput = "04:30 PM";
        Search newSearch = new Search("", new ArrayList<Course>(), new ArrayList<Filter>());
        boolean result = newSearch.isTimeBetween(userInput, startTime, endTime);
        assertFalse(result);
    }

    @Test
    void timeBetweenOneAMOnePMIn() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("10:30:00 AM");
        Date endTime = dateFormat.parse("02:20:00 PM");
        String userInput = "12:10 PM";
        Search newSearch = new Search("", new ArrayList<Course>(), new ArrayList<Filter>());
        boolean result = newSearch.isTimeBetween(userInput, startTime, endTime);
        assertTrue(result);
    }

    @Test
    void timeBetweenOneAMOnePMOut() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("10:30:00 AM");
        Date endTime = dateFormat.parse("02:20:00 PM");
        String userInput = "02:40 PM";
        Search newSearch = new Search("", new ArrayList<Course>(), new ArrayList<Filter>());
        boolean result = newSearch.isTimeBetween(userInput, startTime, endTime);
        assertFalse(result);
    }

    @Test
    void searchTimeIn() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date startTime = dateFormat.parse("10:30:00 AM");
        Date endTime = dateFormat.parse("02:20:00 PM");
        ArrayList<String> userTimes = new ArrayList<String>();
        userTimes.add("11:00 AM");
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
        Date startTime = dateFormat.parse("10:30:00 AM");
        Date endTime = dateFormat.parse("02:20:00 PM");
        ArrayList<String> userTimes = new ArrayList<String>();
        userTimes.add("02:00 PM");
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