package classes;

import classes.Course;
import classes.Filter;

import java.util.ArrayList;

public class Search {
    private String query;
    private ArrayList<Course> results;
    private ArrayList<Filter> appliedFilters;

    public Search(String query, ArrayList<Course> results, ArrayList<Filter> appliedFilters) {
        this.query = query;
        this.results = results;
        this.appliedFilters = appliedFilters;
    }

    /**
     * Adds the specified filter to the appliedFilters ArrayList, applies the filter to
     * the search results using the search() method, and returns the results
     * @param filter is the filter to apply
     * @return the results of the filter application
     */
    public ArrayList<Course> addFilter(Filter filter){
        appliedFilters.add(filter);
        return search(appliedFilters);
    }

    /**
     * Removes the specified filter from the appliedFilters ArrayList, un-applies the filter
     * from the search results by running the search() method without the filter, and returns
     * the results.
     * @param filter is the filter to remove
     * @return the results of the filter removal
     */
    public ArrayList<Course> removeFilter(Filter filter){
        appliedFilters.remove(filter);
        return search(appliedFilters);
    }

    /**
     * Searches courses based on the query provided
     * @param query is the string to search by
     * @return the results of the search
     */
    public ArrayList<Course> search(String query){
        return null;
    }

    /**
     * Searches based on the list of applied filters
     * @param filters is the list of filters to search by
     * @return the results of the search
     */
    public ArrayList<Course> search(ArrayList<Filter> filters){
        Main main = new Main();
        //ArrayList<Course> allCourses = (ArrayList<Course>) main.getCourseCatalog().values();
        ArrayList<Course> allCourses = this.results;
        for(Filter filter : filters){
            if(filter.getType() == Filter.FilterType.DAY){
                ArrayList<Integer> days = new ArrayList<>();
                filter.getInput().forEach(filt -> days.add(Integer.parseInt(filt)));
                for(Course course : allCourses){
                    for(Integer day : days) {
                        if (course.getMeetingTimes()[day] == null){
                            allCourses.remove(course);
                        }
                    }
                }
            }
            else if(filter.getType() == Filter.FilterType.TIME){
                //filter by time here
            }
        }
        return allCourses;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArrayList<Course> getResults() {
        return results;
    }

    public void setResults(ArrayList<Course> results) {
        this.results = results;
    }

    public ArrayList<Filter> getAppliedFilters() {
        return appliedFilters;
    }

    public void setAppliedFilters(ArrayList<Filter> appliedFilters) {
        this.appliedFilters = appliedFilters;
    }
}
