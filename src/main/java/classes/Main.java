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

        searchCourses();

    }

    private static void searchCourses(){
        Scanner input = new Scanner(System.in);
        String query = "";
        ArrayList<Course> results = new ArrayList<>();
        ArrayList<Filter> filters = new ArrayList<>();
        Search s = new Search("",courseCatalog, filters);

        while(!query.equals("Q")){
            System.out.println("-----Course Search-----");
            System.out.println("To leave the search type 'Q'");
            System.out.println("To apply or remove a filter type 'F'");
            System.out.println("Type a course code or name to search here: ");
            query = input.nextLine();

            if(query.equals("Q")){
                return;
            }
            if(query.equals("F")){
                System.out.println("-----Applied Filters-----");
                for(int i = 0; i < filters.size(); i++){
                    System.out.println(filters.get(i).getType() + " - " + filters.get(i).getInput());
                }
                System.out.println("To remove a filter type 'R'");
                System.out.println("To remove all filters type 'Rall'");
                System.out.println("To add a filter type 'A'");
                System.out.println("To go back to search type 'B'");
                query = input.nextLine();
                if(query.equals("R")){
                    System.out.println("Type the number of the filter to remove (or 'B' to go back): ");
                    query = input.nextLine();
                    //TODO: remove the filter
                } else if(query.equals("Rall")){
                    filters.removeAll(filters);
                } else if(query.equals("A")){
                    Filter f = addFilter();
                    if(f != null){
                        s.addFilter(f);
                    }
                }
            } else {
                results = s.search(query);
                results = s.search(filters);
                for(Course c : results){
                    System.out.println(c.getCourseCode() + " " + c.getName());
                }
            }
        }
    }

    private static Filter addFilter(){
        Scanner in = new Scanner(System.in);
        System.out.println("To add a time filter typ 'T', to add a day filter type 'D'");
        String type = in.next();

        if(type.equals("D")){
            ArrayList<String> days = new ArrayList<>();
            System.out.println("Which days do you want to filter by? (format: 'M W F')");
            String inDays = in.next();
            Scanner parser = new Scanner(inDays);
            parser.useDelimiter(" ");
            while(parser.hasNext()){
                days.add(parser.next());
            }
            return new Filter(days, Filter.FilterType.DAY);
        } else if(type.equals("T")){
            System.out.println("Enter start time (format: hh:mm:ss a)");
            Scanner times = new Scanner(System.in);
            ArrayList<String> filterTimes = new ArrayList<>();
            filterTimes.add(times.next());
            System.out.println("Enter end time:");
            filterTimes.add(times.next());
            return new Filter(filterTimes, Filter.FilterType.TIME);
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
                times = new Date[2][5];
                for(int i = 0; i < days.length; i++) {
                    if (days[i]) {
                        times[0][i] = dateFormat.parse(startTime);
                        times[1][i] = dateFormat.parse(endTime);
                    }else{
                        times[0][i] = null;
                        times[1][i] = null;
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
