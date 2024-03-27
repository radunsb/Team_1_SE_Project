package classes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        //get list of courses to further cut down with filters
        ArrayList<Course> allCourses = this.results;
        for(Filter filter : filters){
            switch(filter.getType()){
                case DAY:
                    allCourses = filterDay(allCourses, filter);
                    break;
                case TIME:
                    allCourses = filterTime(allCourses, filter);
                    break;
                default:
                    break;
            }
        }
        this.results = allCourses;
        return allCourses;
    }

    public ArrayList<Course> filterDay(ArrayList<Course> courses, Filter filter){
        ArrayList<Integer> days = new ArrayList<>();
        filter.getInput().forEach(filt -> {switch(filt.toUpperCase()){
            case "M":
                days.add(0); break;
            case "T":
                days.add(1); break;
            case "W":
                days.add(2); break;
            case "R":
                days.add(3); break;
            case "F":
                days.add(4); break;
            default: break;
        }});
        ArrayList<Course> toRemove = new ArrayList<>();
        for(Course course : courses){
            for(Integer day : days) {
                if (!(course.getMeetingDays()[day])){
                    toRemove.add(course);
                }
            }
        }
        courses.removeAll(toRemove);
        return courses;
    }

    public ArrayList<Course> filterTime(ArrayList<Course> courses, Filter filter){
        ArrayList<Course> toKeep = new ArrayList<>();
        String startTime = filter.getInput().get(0);
        String endTime = filter.getInput().get(1);
        for(Course course : courses){
            for(Date[] times : course.getMeetingTimes()){
                if(isTimeBetween(startTime, times[0], times[1]) && isTimeBetween(endTime, times[0], times[1])){
                    toKeep.add(course);
                    break;
                }
            }
        }
        return toKeep;
    }

    public Boolean isTimeBetween(String userInputTime, Date startTime, Date endTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String start = dateFormat.format(startTime);
        String end = dateFormat.format(endTime);
        String userTime = userInputTime.split(" ")[0];
        String userAMPM = userInputTime.split(" ")[1];
        double startNum = Integer.parseInt((start.split(":"))[0])%12 + (Integer.parseInt((start.split(":"))[1])/60.0)
                + ((start.split(" ")[1].equals("PM")) ? 12 : 0);
        double endNum = Integer.parseInt((end.split(":"))[0])%12 + (Integer.parseInt((end.split(":"))[1])/60.0)
                + ((end.split(" ")[1].equals("PM")) ? 12 : 0);
        double userNum = Integer.parseInt((userTime.split(":"))[0])%12 + (Integer.parseInt((userTime.split(":"))[1])/60.0)
                + (userAMPM.equals("PM") ? 12 : 0);
        return (startNum <= userNum && userNum <= endNum);
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
