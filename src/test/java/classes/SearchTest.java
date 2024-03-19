package classes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class SearchTest {

    @Test
    void testSearchQuery(){
        // TESTS SEARCH BY COURSE CODE AND COURSE NAME
        ArrayList<Course> catalog = new ArrayList<>();
        catalog.add(new Course("ACCT101A","accounting",null,null,null,null, null, null, null));
        catalog.add(new Course("MATH101A","COMPUTER CALCULUS",null,null,null,null, null, null, null));
        catalog.add(new Course("COMP101A","COMPUTER SCIENCE",null,null,null,null, null, null, null));
        catalog.add(new Course("BIOL101A","BIOLOGY",null,null,null,null, null, null, null));

        Search s = new Search("", catalog, null);
        ArrayList<Course> results = s.search("accoun");
        assertEquals(results.get(0).getCourseCode(), "ACCT101A");
        results = s.search("comp");
        assertEquals(results.get(0).getCourseCode(), "MATH101A");
        assertEquals(results.get(1).getCourseCode(),"COMP101A");
        results = s.search("101A");
        assertEquals(results.get(0).getCourseCode(), "ACCT101A");
        assertEquals(results.get(1).getCourseCode(),"MATH101A");
        assertEquals(results.get(2).getCourseCode(), "COMP101A");
        assertEquals(results.get(3).getCourseCode(),"BIOL101A");
        results = s.search("calc");
        assertEquals(results.get(0).getCourseCode(), "MATH101A");
    }
}
