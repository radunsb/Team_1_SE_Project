package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static Schedule currentSchedule;
    private static ArrayList<Course> courseCatalog;


    public static void main(String[] args) {
        run();
    }

    public static void run(){
        // Read the CSV
        try{
            readCSV();
        } catch(FileNotFoundException | ParseException e){
            System.out.println(e.getMessage());
        }

        // Sanity Testing: I tried it with pretty much all the attributes, and they all seem good
        // Since its kind of hard to unit test it lol
//        for(Course c: courseCatalog){
//            if(c.getMeetingTimes() != null) {
//                System.out.println(c.getMeetingTimes()[0][0]);
//            }
//        }

    }

    private static void searchCourses(){
        //User input setup
        Scanner input = new Scanner(System.in);
        String query = "";
        ArrayList<Course> results;
        ArrayList<Filter> filters = new ArrayList<>();
        // Create search instance
        Search s = new Search("", courseCatalog, filters);

        while(!query.equals("Q")){
            // Search info and navigation info
            System.out.println("-----Course Search-----");
            System.out.println("To leave the search type 'Q'");
            System.out.println("To apply or remove a filter type 'F'");
            System.out.println("Type a course code or name to search here: ");
            query = input.nextLine();

            // Quit -> exit search
            if(query.equals("Q")){
                return;
            }
            // Filter navigation and info
            if(query.equals("F")){
                // Print all applied filters with label numbers
                System.out.println("-----Applied Filters-----");
                for(int i = 0; i < filters.size(); i++){
                    System.out.println("Filter " + i + ": " + filters.get(i).getType());
                }
                System.out.println("To remove a filter type 'R'");
                System.out.println("To remove all filters type 'Rall'");
                System.out.println("To add a filter type 'A'");
                System.out.println("To go back to search type 'B'");
                query = input.nextLine();

                if(query.equals("R")){
                    // Remove filter
                    removeFilter(s, filters);
                } else if(query.equals("Rall")){
                    // Clear the filter ArrayList
                    filters.removeAll(filters);
                } else if(query.equals("A")){
                    // Add filter
                    Filter f = addFilter(filters);
                    if(f != null){
                        s.addFilter(f);
                    }
                }
            } else {
                // Search on the query
                results = s.search(query);
                results = s.search(filters);
                if(results.isEmpty()){
                    System.out.println("No courses match your search.");
                }
                for(Course c : results){
                    System.out.println(displayCourse(c));
                }
            }
        }
    }

    private static String displayCourse(Course c){
        String s = c.getCourseCode() + " " + c.getName();
        for(int i = 0; i < c.getMeetingDays().length; i++){
            boolean day = c.getMeetingDays()[i];
            if(day){
                if(i == 0){
                    s += " M";
                }else if(i == 1){
                    s += " T";
                }else if(i == 2){
                    s += " W";
                }else if(i == 3){
                    s += " R";
                }else if(i == 4){
                    s += " F";
                }
            }
        }
        if(c.getMeetingTimes() != null) {
            boolean b = false;
            for(int k = 0; k < 5; k++) {
                if(b){
                    break;
                }
                for (int i = 0; i < 2; i++) {
                    Date[] times = c.getMeetingTimes()[i];
                    if (times[0] != null) {
                        s += " " + times[0] + " " + times[1];
                        b = true;
                        break;
                    }
                }
            }
        }
        return s;
    }
    private static void removeFilter(Search s, ArrayList<Filter> filters){
        Scanner input = new Scanner(System.in);
        if(!filters.isEmpty()) {
            System.out.println("Type the number of the filter to remove (or any non-integer character to go back): ");
            int f = -1;
            boolean isInt = true;
            while (f == -1) {
                try {
                    f = input.nextInt();
                } catch (Exception e) {
                    isInt = false;
                    break;
                }
                if (f >= filters.size() || f < 0) {
                    System.out.println("Invalid Integer, try again");
                    f = -1;
                }
            }
            if(isInt) {
                s.removeFilter(filters.get(f));
            }
        }else{
            System.out.println("There are no applied filters.");
        }
    }
    private static Filter addFilter(ArrayList<Filter> filters){
        Scanner in = new Scanner(System.in);
        System.out.println("To add a time filter typ 'T', to add a day filter type 'D'");
        String type = in.next();
        boolean filterApplied = false;

        // Day filter
        if(type.equals("D")){
            // Check if already applied
            for(Filter f : filters){
                if(f.getType() == Filter.FilterType.DAY){
                    System.out.println("Cannot have two day filters at once.");
                    filterApplied = true;
                }
            }
            if(!filterApplied) {
                // Get the filter
                ArrayList<String> days = new ArrayList<>();
                System.out.println("Which days do you want to filter by? (format: 'M W F')");
                String inDays = in.next();
                Scanner parser = new Scanner(inDays);
                parser.useDelimiter(" ");
                while (parser.hasNext()) {
                    String day = parser.next().toUpperCase();
                    if(day.equals("M") || day.equals("T") || day.equals("W") || day.equals("R") || day.equals("F")){
                        days.add(day);
                    }
                }
                return new Filter(days, Filter.FilterType.DAY);
            }
        } else if(type.equals("T")){
            // Check if already applied
            for(Filter f : filters){
                if(f.getType() == Filter.FilterType.DAY){
                    System.out.println("Cannot have two time filters at once.");
                    filterApplied = true;
                }
            }
            if(!filterApplied) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                ArrayList<String> filterTimes = new ArrayList<>();
                Scanner times = new Scanner(System.in);
                while(true) {
                    System.out.println("Enter start time (format: hh:mm a)");
                    String start = times.nextLine();
                    try{
                        dateFormat.parse(start);
                        filterTimes.add(start);
                        break;
                    }catch(Exception e){
                        System.out.println("Invalid Time, try again");
                    }
                }
                while(true){
                    System.out.println("Enter end time:");
                    String end = times.nextLine();
                    try{
                        dateFormat.parse(end);
                        filterTimes.add(end);
                        break;
                    }catch(Exception e){
                        System.out.println("Invalid Time, try again");
                    }
                }
                return new Filter(filterTimes, Filter.FilterType.TIME);
            }
        }
        return null;
    }

    /**
     * Reads the 2020-2021.csv file and parses the data into an ArrayList of course objects
     * @throws FileNotFoundException if the file isn't found
     * @throws ParseException if something weird happens (it shouldn't, hopefully. . .)
     */
    private static void readCSV() throws FileNotFoundException, ParseException {
        courseCatalog = new ArrayList<>();

        Scanner scnr = new Scanner(new File("2020-2021.csv"));
        // Get the header line and skip it
        String headLine = scnr.nextLine();

        while(scnr.hasNext()){
            Scanner line = new Scanner(scnr.nextLine());
            line.useDelimiter(",");

            int year = line.nextInt();
            int term = line.nextInt();
            Course.Semester semester;
            if(term == 10){
                semester = Course.Semester.FALL;
            }else{
                semester = Course.Semester.SPRING;
            }
            StringBuilder code = new StringBuilder();
            code.append(line.next());
            code.append(line.next());
            code.append(line.next());
            String courseName = line.next();
            int credits = line.nextInt();
            int capacity = line.nextInt();
            // NOT USING ENROLLMENT
            int enrollment = line.nextInt();
            boolean[] days = new boolean[5];
            for(int i = 0; i < 5; i++){
                String day = line.next();
                if(day.isEmpty()){
                    days[i] = false;
                }
                else{
                    days[i] = true;
                }
            }

            String startTime = line.next();
            String endTime = line.next();

            Date[][] times = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");

            // Some of the classes don't have times (Online courses)
            if(!startTime.equals("")){
                times = new Date[5][2];
                for(int i = 0; i < days.length; i++) {
                    if (days[i]) {
                        times[i][0] = dateFormat.parse(startTime);
                        times[i][1] = dateFormat.parse(endTime);
                    }else{
                        times[i][0] = null;
                        times[i][1] = null;
                    }
                }
            }

            String profLast = line.next();
            String profFirst = line.next();

            // TODO: If we want the comments off the csv file, they need to be dealt with here
            // TODO: Also need to write the logic for combining weird courses such as calculus -> will do later

            Course newCourse = new Course(code.toString(),
                    courseName,
                    "Not Available",
                    profFirst+" "+profLast,
                    times,
                    days,
                    year,
                    credits,
                    semester,
                    capacity,
                    null,
                    null,
                    null
                    );

            courseCatalog.add(newCourse);

        }

    }

    public ArrayList<Course> getCourseCatalog() {
        return courseCatalog;
    }

}
