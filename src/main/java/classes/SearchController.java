package classes;

import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static void getResultsWithFilters(Context context){
        //Search instance
        Search s = new Search("", Main.getCourseCatalog(), null);
        String query = context.pathParam("query");
        String filters = context.pathParam("filters");
        String[] filterList = filters.split(",");
        ArrayList<Filter> actualFilters = new ArrayList<Filter>();
        for(int i = 0; i < filterList.length; i++){
            String type = filterList[i].split("& ")[0].toUpperCase();
            ArrayList<String> input = new ArrayList<String>
                    (Arrays.stream((filterList[i].split("& ")[1].trim().split("\\|"))).toList());
            Filter f = new Filter(input, Filter.FilterType.valueOf(type));
            actualFilters.add(f);
        }
        s.search(query);
        ArrayList<Course> results;
        results = s.search(actualFilters);
        context.json(results);
    }

}
