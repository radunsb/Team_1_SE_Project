package classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    @Test
    void addFilterDay() {

    }

    @Test
    void removeFilter() {
    }

    @Test
    void searchFilterDay() {
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
        daysWanted.clear();
        daysWanted.add("T");
        Filter tuesday = new Filter(daysWanted, Filter.FilterType.DAY);

        filters.add(tuesday);
        endCourses.clear();
        endCourses = newSearch.search(filters);
        assertEquals(0, endCourses.size());
    }

}