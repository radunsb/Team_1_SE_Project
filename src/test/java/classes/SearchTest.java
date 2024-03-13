package classes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    @Test
    void addFilterDay() {
        Date[][] mondaysOnly = {{new Date(), new Date()}, {}, {}, {}, {}};
        ArrayList<String> daysWanted = new ArrayList<>();
        daysWanted.add("1");
        char[] days = new char[5];
        ArrayList<String> emptyList = new ArrayList<>();
        Course testCourse1 = new Course("1", "test1", "test course",
                "James Borg", mondaysOnly, days, emptyList, emptyList, emptyList);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(testCourse1);
        Filter monday = new Filter(daysWanted, Filter.FilterType.DAY);
        ArrayList<Filter> filters = new ArrayList<>();
        Search search = new Search("", courses, filters);
        ArrayList<Course> endCourses = search.addFilter(monday);
        assertEquals(1, endCourses.size());
    }

    @Test
    void removeFilter() {
    }

    @Test
    void searchFilterDay() {
        Date[][] mondaysOnly = {{new Date(), new Date()}, {}, {}, {}, {}};
        ArrayList<String> daysWanted = new ArrayList<>();
        daysWanted.add("1");
        char[] days = new char[5];
        ArrayList<String> emptyList = new ArrayList<>();
        Course testCourse1 = new Course("1", "test1", "test course",
                "James Borg", mondaysOnly, days, emptyList, emptyList, emptyList);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(testCourse1);
        Filter monday = new Filter(daysWanted, Filter.FilterType.DAY);
        daysWanted.clear();
        daysWanted.add("2");
        Filter tuesday = new Filter(daysWanted, Filter.FilterType.DAY);
        ArrayList<Filter> filters = new ArrayList<>();
        Search search = new Search("", courses, filters);
        filters.add(monday);
        ArrayList<Course> endCourses = search.search(filters);
        assertEquals(1, endCourses.size());
        filters.add(tuesday);
        endCourses.clear();
        endCourses = search.search(filters);
        assertEquals(0, endCourses.size());
    }
}