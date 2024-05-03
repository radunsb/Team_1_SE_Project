package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.*;



public class PDFBoxToText {

    private static ArrayList<Course> recomendedCourses;
    private static ArrayList<Course> courseCatalog;
    private static Schedule currentSchedule;

    private static ArrayList<Course> recomendedSpring;
    private static ArrayList<Course> recomendedFall;



    public static void main(String[] args) {

        currentSchedule = new Schedule(1,"Spring",2020,"default");
        try{
            readCSV();
        } catch(FileNotFoundException | ParseException e){
            System.out.println(e.getMessage());
        }

        String ni = PremadeScheduleString("CS.pdf");

        String[] courses = ni.split("\n");

        String fall = makeFall(courses);
        String spring = makeSpring(courses);

        recomendedSpring = new ArrayList<Course>();
        recomendedFall = new ArrayList<Course>();

        makeRecomended(fall, recomendedFall);
        makeRecomended(spring, recomendedSpring);

        System.out.println(recomendedFall);
        System.out.println();
        System.out.println(recomendedSpring);

    }

    public static void makeRecomended(String s, ArrayList<Course> sched){
        String[] courses = s.split("\n");

        for(String t:courses){
            if(t.contains("…") || t.contains("&")){

            }else{
                searchRecomended(t,sched);
            }
        }
    }
    public static String makeSpring(String[] s){
        StringBuilder st = new StringBuilder();

        for (int i = 1; i < s.length; i = i+2) {
            st.append(s[i]);
            st.append("\n");
        }
        return st.toString();
    }

    public static String makeFall(String[] s){
        StringBuilder st = new StringBuilder();

        for (int i = 0; i < s.length; i = i+2) {
            st.append(s[i]);
            st.append("\n");
        }
        return st.toString();
    }

    private static String cleanString(String s){
        if(!s.contains("\r\n")){
            return s;
        }
        String segments[] = s.split("\r\n");
        return segments[segments.length-1];
    }

    private static String PremadeScheduleString(String filepath){
        PDFManager pdfManager = new PDFManager();
        pdfManager.setFilePath(filepath);

        StringBuilder stringBuilder = new StringBuilder();

        try{
            String firstPageText = pdfManager.toText();

            String[] token = firstPageText.split("Credits");
            for(String s:token){
                s.strip();
            }

            for (int i = 0; i < token.length; i++){

                String[] block = token[i].split(" ");
                for (int k = 0; k < block.length; k++) {
                    block[k] = cleanString(block[k]);
                }

                for (int j = 0; j < block.length-1; j++) {

                    if((isAllUpper(block[j]) && !isNumeric(block[j]) && !block[j].contains("…") && !block[j+1].contains("…"))){
                        stringBuilder.append(block[j]);
                        stringBuilder.append(block[j+1]);
                        stringBuilder.append("\n");
                    }
                    if(block[j].equals("Science")){
                        stringBuilder.append("Science Elective");
                        stringBuilder.append("\n");
                    }
                    if(block[j].equals("General")){
                        stringBuilder.append("General Elective");
                        stringBuilder.append("\n");
                    }
                }
            }
            return stringBuilder.toString();

        }catch(IOException e){
            System.out.println("it Broke");
            return "";
        }
    }

    static void readCSV() throws FileNotFoundException, ParseException {
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

    private static void searchRecomended(String q, ArrayList<Course> sched) {
        //User input setup
        String query = q;
        ArrayList<Filter> filters = new ArrayList<>();

        Filter semesterFilter = new Filter(new ArrayList<String>(List.of(currentSchedule.getSemester(),"" + currentSchedule.getYear())), Filter.FilterType.SEMESTER);

        // Create search instance
        Search s = new Search("", courseCatalog, filters);

            if (query.equals("Q")) {
                return;
            }
            if (query.equals("F")) {
                return;
            } else {
                // Search on the query
                ArrayList<Course> results = s.search(query);
                if (results.isEmpty()) {
                    return;
                }
                try {
                    sched.add(results.get(0));
                    return;
                } catch (Exception e) {
                    // Clear the input and do nothing -> go back to search
                    return;
                }
            }

    }

    private static void removeFilter(Search s, ArrayList<Filter> filters){
        Scanner input = new Scanner(System.in);
        if(!filters.isEmpty()) {
            System.out.println("Type the number of the filter to remove (or any non-integer character to go back): ");
            int f = -1; // starter value for error checking
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
                if(f.getType() == Filter.FilterType.TIME){
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

    private static String displayCourse(Course c){

        String s = c.getCourseCode() + " " + String.format("%1$40s", c.getName());
        String days = "";
        for(int i = 0; i < c.getMeetingDays().length; i++){
            boolean day = c.getMeetingDays()[i];
            if(day){
                if(i == 0){
                    days += " M";
                }else if(i == 1){
                    days += " T";
                }else if(i == 2){
                    days += " W";
                }else if(i == 3){
                    days += " R";
                }else if(i == 4){
                    days += " F";
                }
            }
        }
        s += String.format("%1$10s", days);
        s += "  ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        if(c.getMeetingTimes() != null) {
            boolean b = false; // tells if we need to break -> i.e. we have the time
            for(int k = 0; k < 5; k++) {
                if(b){
                    break;
                }
                for (int i = 0; i < 5; i++) {
                    Date[] times = c.getMeetingTimes()[i];
                    if (times[0] != null) {
                        s += " " + dateFormat.format(times[0]) + " - " + dateFormat.format(times[1]);
                        b = true;
                        break;
                    }
                }
            }
        }
        return s;
    }

    private static boolean isAllUpper(String s) {
        for(char c : s.toCharArray()) {
            if(Character.isLetter(c) && Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}