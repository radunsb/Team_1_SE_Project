package classes;

import io.javalin.http.Context;

import java.util.ArrayList;

public class SearchController {

    public static void emptySearch(Context context){
        // Search instance
        Search s = new Search("", Main.getCourseCatalog(), null);
        ArrayList<Course> results = s.search("");
        context.json(results);
    }
    public static void getResults(Context context) {
        // Search instance
        Search s = new Search("", Main.getCourseCatalog(), null);

        // query
        String query = context.pathParam("query");

        // results
        ArrayList<Course> results = s.search(query);
        context.json(results);
    }

}
