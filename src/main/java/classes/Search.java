package classes;

import classes.Course;
import classes.Filter;

import java.util.ArrayList;

public class Search {
    private String query;
    private ArrayList<Course> courseCatalog;
    private ArrayList<Course> results;
    private ArrayList<Filter> appliedFilters;

    public Search(String query, ArrayList<Course> courseCatalog, ArrayList<Filter> appliedFilters) {
        this.query = query;
        this.courseCatalog = courseCatalog;
        this.appliedFilters = appliedFilters;
        this.results = courseCatalog;
    }

    /**
     * Adds the specified filter to the appliedFilters ArrayList, applies the filter to
     * the search results using the search() method, and returns the results
     * @param filter is the filter to apply
     * @return the results of the filter application
     */
    public ArrayList<Course> addFilter(Filter filter){
        return null;
    }

    /**
     * Removes the specified filter from the appliedFilters ArrayList, un-applies the filter
     * from the search results by running the search() method without the filter, and returns
     * the results.
     * @param filter is the filter to remove
     * @return the results of the filter removal
     */
    public ArrayList<Course> removeFilter(Filter filter){
        return null;
    }

    /**
     * Searches course codes and names for matches of the provided query
     * @param query is the string to search by
     * @return the results of the search
     */
    public ArrayList<Course> search(String query){
        ArrayList<Course> newResults = new ArrayList<>();
        query = query.toUpperCase();
        for(Course course : courseCatalog){
            if(course.getName().toUpperCase().contains(query) || course.getCourseCode().toUpperCase().contains(query)){
                newResults.add(course);
            }
        }
        results = newResults;
        return newResults;
    }

    /**
     * Searches based on the list of applied filters
     * @param filters is the list of filters to search by
     * @return the results of the search
     */
    public ArrayList<Course> search(ArrayList<Filter> filters){
        return null;
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
