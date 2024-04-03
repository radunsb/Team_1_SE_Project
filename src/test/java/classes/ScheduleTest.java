package classes;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    void addCourse() {
        assertEquals(0, 1);
    }

    @Test
    void removeCourse() {
        assertEquals(0,1);
    }

    @Test
    void testToString() {
        assertEquals(0, 1);
    }

    @Test
    void testSaveSchedule() throws FileNotFoundException, ParseException {
        Main main = new Main();
        Main.readCSV();
        Search newSearch = new Search("Introduction to Sculpture", main.getCourseCatalog(), new ArrayList<>());
        ArrayList<Course> courses = newSearch.search(newSearch.getQuery());
        Schedule sched = new Schedule(1, "Fall", 2020, "Potatoes");
        sched.addCourse(courses.get(0));
        sched.saveSchedule("Jeff");
        File outputFile = new File("Jeff",sched.getScheduleID() + "_" + sched.getScheduleName());
        assertTrue(outputFile.exists());
        Scanner scan = new Scanner(outputFile);
        StringBuilder thingy = new StringBuilder();
        while(scan.hasNext()){
            thingy.append(scan.next());
        }
        String endString = thingy.toString();
        assertEquals("1,Fall,2020,Potatoes,ART111A", endString);
    }

    @Test
    void testLoadSchedule() throws FileNotFoundException, ParseException{
        Main.readCSV();
        testSaveSchedule();
        String inputFile = "1_Potatoes";
        Schedule sched = new Schedule(2, "FALL", 2020, "TempName");
        sched.loadSchedule("Jeff", inputFile);
        Scanner inScan = new Scanner(inputFile);
        System.out.println(inScan.next());
        assertEquals(1, sched.getScheduleID());
        Course artCourse = sched.getCourses().get(0);
        assertEquals("ART111A", artCourse.getCourseCode());
    }
}