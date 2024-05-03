package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class RecomendedSchedule {

    private String filePath;
    private static ArrayList<Course> courseCatalog;
    private Schedule currentSchedule;
    private Schedule recomendedSpringFreshman;
    private Schedule recomendedSpringSophmore;
    private Schedule recomendedSpringJunior;
    private Schedule recomendedSpringSenior;
    private Schedule recomendedFallFreshman;
    private Schedule recomendedFallSopohmore;
    private Schedule recomendedFallJunior;
    private Schedule recomendedFallSenior;

    public RecomendedSchedule(String filePath){
        this.filePath = filePath;
    }

    public void makeRecomended(){
        try{
            readCSV();
        } catch(FileNotFoundException | ParseException e){
            System.out.println(e.getMessage());
        }

        String workingString = PremadeScheduleString(filePath);

        String[] courses = workingString.split("\n");

        String fall = makeFall(courses);
        String spring = makeSpring(courses);

        makeRecomended(fall,recomendedFallFreshman,0,4);
        makeRecomended(fall,recomendedFallSopohmore,5,11);
        //change these to spring
        makeRecomended(fall,recomendedSpringJunior,12,17);
        makeRecomended(fall,recomendedFallSenior,18,22);

        makeRecomended(spring,recomendedSpringFreshman,0,4);
        makeRecomended(spring,recomendedSpringSophmore,5,9);
        makeRecomended(spring,recomendedFallJunior,11,16);
        makeRecomended(spring,recomendedSpringSenior,17,22);

    }

    /**
     * returns ruff string from PDF for filtering into schedules
     * @param filepath
     * @return ruffString
     */
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

    /**
     * Uses filtered string repeditely call searchmethod to fill
     * example schedules
     * @param s
     * @param sched
     * @param start
     * @param end
     */
    public static void makeRecomended(String s, Schedule sched, int start, int end){
        String[] courses = s.split("\n");

        for (int i = start; i < end; i++) {
            if(courses[i].contains("…") || courses[i].contains("&")){

            }else{
                searchRecomended(courses[i],sched);
            }
        }
    }

    /**
     * modified couse search method for creating default example schedule
     * @param q
     * @param sched
     */
    private static void searchRecomended(String q, Schedule sched) {
        //User input setup
        String query = q;
        ArrayList<Filter> filters = new ArrayList<>();

        //Filter semesterFilter = new Filter(new ArrayList<String>(List.of(currentSchedule.getSemester(),"" + currentSchedule.getYear())), Filter.FilterType.SEMESTER);

        // Create search instance
        Search s = new Search("", courseCatalog, filters);

        if (query.equals("Q")) {
            return;
        }
        else if(query.equals("Science Elective")){
            //add blank course
        }else if(query.equals("General Elective")){
            //add blank general elective
        }
        else {
            // Search on the query
            ArrayList<Course> results = s.search(query);
            if (results.isEmpty()) {
                return;
            }
            try {
                sched.addCourse(results.get(0));
                return;
            } catch (Exception e) {
                // Clear the input and do nothing -> go back to search
                return;
            }
        }

    }

    /**
     * Makes string for spring semester classes
     * @param s
     * @return
     */
    public static String makeSpring(String[] s){
        StringBuilder st = new StringBuilder();

        for (int i = 1; i < s.length; i = i+2) {
            st.append(s[i]);
            st.append("\n");
        }
        return st.toString();
    }

    /**
     * Makes string for fall semester classes
     * @param s
     * @return
     */
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

    /**
     * reads CSV file to make course catalog
     * @throws FileNotFoundException
     * @throws ParseException
     */
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

    public Schedule getRecomendedSpringFreshman() {
        return recomendedSpringFreshman;
    }

    public Schedule getRecomendedSpringSophmore() {
        return recomendedSpringSophmore;
    }

    public Schedule getRecomendedSpringJunior() {
        return recomendedSpringJunior;
    }

    public Schedule getRecomendedSpringSenior() {
        return recomendedSpringSenior;
    }

    public Schedule getRecomendedFallFreshman() {
        return recomendedFallFreshman;
    }

    public Schedule getRecomendedFallSopohmore() {
        return recomendedFallSopohmore;
    }


    public Schedule getRecomendedFallJunior() {
        return recomendedFallJunior;
    }

    public Schedule getRecomendedFallSenior() {
        return recomendedFallSenior;
    }
}
