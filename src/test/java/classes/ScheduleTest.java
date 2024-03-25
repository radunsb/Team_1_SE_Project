package classes;

import org.junit.jupiter.api.Test;

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
    void testSaveSchedule() throws FileNotFoundException {
        Date[][] times = {{},{},{},{},{}};
        boolean[] days = {false, false, false, false, false};
        ArrayList<String> emptyString = new ArrayList<>();
        Course newCourse = new Course("COMP244B", "Computer Stuff", "James Borg",
                "James Borg", times, days, 2007, 3, Course.Semester.FALL,
                30, emptyString, emptyString, emptyString);
        Schedule sched = new Schedule(1, "Fall2024", "Potatoes");
        sched.addCourse(newCourse);
        sched.saveSchedule();
        File outputFile = new File(sched.getScheduleID() + "_" + sched.getScheduleName());
        assertTrue(outputFile.exists());
        Scanner scan = new Scanner(outputFile);
        StringBuilder thingy = new StringBuilder();
        while(scan.hasNext()){
            thingy.append(scan.next());
        }
        String endString = thingy.toString();
        assertEquals("1$Fall2024$Potatoes$COMP244B", endString);
    }

}