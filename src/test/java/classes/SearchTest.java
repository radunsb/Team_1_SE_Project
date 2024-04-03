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
        Date startTime = dateFormat.parse("11:30:00 AM");
        Date endTime = dateFormat.parse("12:30:00 PM");
        ArrayList<String> userTimes = new ArrayList<String>();
        userTimes.add("10:00 AM");
        userTimes.add("1:50 PM");
        Date[][] dates = {{null, null},{null, null},{startTime, endTime},{startTime, endTime},{startTime, endTime}};
        boolean[] days = {false, false, true, true, true};
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
        userTimes.add("11:00 AM");
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

    @Test
    public void searchOneByName(){
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("ACCT101A", "INTRO TO ACCOUNTING", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("MATH101A", "precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("comp201A", "computer science 1", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("biol201A", "BIOLOGY LAB", null, null, null, null, 2020, 3, null, 30, null, null, null));
        Search s = new Search("",courses,null);

        ArrayList<Course> expected = new ArrayList<>();
        expected.add(new Course("MATH101A", "precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));

        assertEquals(expected.get(0).getCourseCode(), s.search("pre").get(0).getCourseCode());
        assertEquals(expected.get(0).getCourseCode(), s.search("PRECALC").get(0).getCourseCode());
        assertEquals(expected.get(0).getCourseCode(), s.search("calculus").get(0).getCourseCode());
    }

    @Test
    public void searchMultipleByName(){
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("ACCT101A", "INTRO TO ACCOUNTING", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("MATH101A", "intro to precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("comp201A", "computer science 1", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("biol201A", "BIOLOGY LAB", null, null, null, null, 2020, 3, null, 30, null, null, null));
        Search s = new Search("",courses,null);

        ArrayList<Course> expected = new ArrayList<>();
        expected.add(new Course("ACCT101A", "INTRO TO ACCOUNTING", null, null, null, null, 2020, 3, null, 30, null, null, null));
        expected.add(new Course("MATH101A", "intro to precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));

        ArrayList<Course> result = s.search("intro");
        assertEquals(expected.get(0).getCourseCode(), result.get(0).getCourseCode());
        assertEquals(expected.get(1).getCourseCode(), result.get(1).getCourseCode());
        ArrayList<Course> result2 = s.search("INTR");
        assertEquals(expected.get(0).getCourseCode(), result2.get(0).getCourseCode());
        assertEquals(expected.get(1).getCourseCode(), result2.get(1).getCourseCode());
    }

    @Test
    public void searchOneByCode(){
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("ACCT101A", "INTRO TO ACCOUNTING", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("MATH102A", "intro to precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("comp201A", "computer science 1", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("biol202A", "BIOLOGY LAB", null, null, null, null, 2020, 3, null, 30, null, null, null));
        Search s = new Search("",courses,null);

        ArrayList<Course> expected = new ArrayList<>();
        expected.add(new Course("MATH102A", "intro to precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));

        assertEquals(expected.get(0).getCourseCode(), s.search("MATH").get(0).getCourseCode());
        assertEquals(expected.get(0).getCourseCode(), s.search("102A").get(0).getCourseCode());
        assertEquals(expected.get(0).getCourseCode(), s.search("102a").get(0).getCourseCode());
    }

    @Test
    public void searchMultipleByCode(){
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("ACCT101A", "INTRO TO ACCOUNTING", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("MATH101A", "intro to precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("comp201A", "computer science 1", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("biol201A", "BIOLOGY LAB", null, null, null, null, 2020, 3, null, 30, null, null, null));
        Search s = new Search("",courses,null);

        ArrayList<Course> expected = new ArrayList<>();
        expected.add(new Course("ACCT101A", "INTRO TO ACCOUNTING", null, null, null, null, 2020, 3, null, 30, null, null, null));
        expected.add(new Course("MATH101A", "intro to precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));

        ArrayList<Course> result = s.search("101A");
        assertEquals(expected.get(0).getCourseCode(), result.get(0).getCourseCode());
        assertEquals(expected.get(1).getCourseCode(), result.get(1).getCourseCode());
    }

    @Test
    public void searchByNameAndCode(){
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("ACCT101A", "INTRO TO ACCOUNTING", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("MATH102A", "intro to precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("comp201A", "computer science 1", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("biol202A", "BIOLOGY LAB", null, null, null, null, 2020, 3, null, 30, null, null, null));
        Search s = new Search("",courses,null);

        ArrayList<Course> expected = new ArrayList<>();
        expected.add(new Course("comp201A", "computer science 1", null, null, null, null, 2020, 3, null, 30, null, null, null));

        assertEquals(expected.get(0).getCourseCode(), s.search("comp").get(0).getCourseCode());
    }

    @Test
    public void searchMultipleByNameAndCode(){
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("ACCT101A", "INTRO TO ACCOUNTING", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("MATH102A", "intro to precalculus", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("comp201A", "computer science 1", null, null, null, null, 2020, 3, null, 30, null, null, null));
        courses.add(new Course("biol202A", "COM BIOLOGY LAB", null, null, null, null, 2020, 3, null, 30, null, null, null));
        Search s = new Search("",courses,null);

        ArrayList<Course> expected = new ArrayList<>();
        expected.add(new Course("comp201A", "computer science 1", null, null, null, null, 2020, 3, null, 30, null, null, null));
        expected.add(new Course("biol202A", "COM BIOLOGY LAB", null, null, null, null, 2020, 3, null, 30, null, null, null));

        assertEquals(expected.get(0).getCourseCode(), s.search("com").get(0).getCourseCode());
        assertEquals(expected.get(1).getCourseCode(), s.search("com").get(1).getCourseCode());
    }

}
