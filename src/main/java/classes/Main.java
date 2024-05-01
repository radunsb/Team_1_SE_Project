package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.*;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Schedule currentSchedule;
    private static Student currentStudent;
    private static ArrayList<Course> courseCatalog;
    private static int schedCount;


    public static void main(String[] args) {
        run();
    }

    //hi
    public static void run() {

        //Read the CSV
        try{
            readCSV();
        } catch(FileNotFoundException | ParseException e){
            System.out.println(e.getMessage());
        }

        Scanner input = new Scanner(System.in);

        int confirm = 0;

        while(true){
            try{
                System.out.println("Hello please enter 1 to create a new user");
                System.out.println("Or Enter 2 to log in as an existing user");
                confirm = input.nextInt();
                if(confirm == 1 || confirm == 2) {
                    break;
                }else{
                    System.out.println("Invalid Input");
                }
            }catch(Exception e){
                System.out.println("Invalid input");
            }
            input.nextLine();
        }
        if (confirm == 1) {
            Student ben = craftUser(input);
            currentStudent = ben;
            File lookForData = new File(ben.getStudentID() + "_" + ben.getUsername());
            schedCount = lookForData.exists() ? Objects.requireNonNull(lookForData.listFiles()).length : 0;
            System.out.println("congratulations " + ben.toString() + " welcome to our app");
            navigateHome(input, ben);
        } else{
            while(true) {
                System.out.println("To create a user instead, input C");
                System.out.println("Please Enter Your ID");
                String id = input.next();
                if(id.equals("C")){
                    Student ben = craftUser(input);
                    currentStudent = ben;
                    File lookForData = new File(ben.getStudentID() + "_" + ben.getUsername());
                    schedCount = lookForData.exists() ? Objects.requireNonNull(lookForData.listFiles()).length : 0;
                    System.out.println("congratulations " + ben.toString() + " welcome to our app");
                    navigateHome(input, ben);
                    break;
                }
                System.out.println("Please Enter Your Name");
                String name = input.next();
                File lookForData = new File(id + "_" + name);
                if(lookForData.exists()){
                    ArrayList<Major> majors = new ArrayList<>();
                    //give no one a minor
                    ArrayList<Minor> minors = new ArrayList<>();
                    //no course history right now
                    ArrayList<Course> courseHistory = new ArrayList<>();
                    //add empty schedule
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    Student ben = new Student(Integer.parseInt(id), name, Student.Class.JUNIOR, majors, minors, courseHistory, schedules);
                    currentStudent = ben;
                    schedCount = Objects.requireNonNull(lookForData.listFiles()).length;
                    navigateHome(input, ben);
                    break;
                }
                else{
                    System.out.println("User does not exist. Try again");
                }
            }

        }

    }

    /**
     * Prompts user to enter in account information to create a new student
     */
    public static Student craftUser(Scanner s) {
        //ask for ID
        int id = 0;
        while(true) {
            try {
                System.out.println("please enter your student ID");
                id = s.nextInt();
                break;
            }catch(Exception e){
                System.out.println("Invalid input");
                s.nextLine();
            }
        }

        //ask for username
        System.out.println("please enter a username");
        String username = s.next();

        ArrayList<Major> majors = new ArrayList<>();

        //give no one a minor
        ArrayList<Minor> minors = new ArrayList<>();

        //no course history right now
        ArrayList<Course> courseHistory = new ArrayList<>();

        //add empty schedule
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Student newStudent = new Student(id, username, Student.Class.JUNIOR, majors, minors, courseHistory, schedules);

        currentStudent = newStudent;

        return newStudent;
    }

    public static void navigateHome(Scanner s, Student current) {
        String command = "";
        current.setSchedules(current.loadAllSchedules(current.getStudentID() + "_" + current.getUsername()));
        if (current.getSchedules().isEmpty()) {
            int year = 2020;
            current.addNewSchedule(0, "Fall", year, "defaultSchedule");
            System.out.println("You did not have any schedules.\nHere is a default schedule\n");
            schedCount++;
        }
        currentSchedule = current.getSchedules().getFirst();
        currentSchedule.saveSchedule(current.getStudentID() + "_" + current.getUsername());

        while (true) {
            System.out.println("You are currently editing: " + currentSchedule.getScheduleName());
            System.out.println();
            System.out.println("Enter 1 to make a new schedule");
            System.out.println("Enter 2 to view current Schedule");
            System.out.println("Enter 3 to search courses");
            System.out.println("Enter 4 to edit Schedules");
            System.out.println("\nEnter EXIT to log out");

            command = s.next();

            if (command.equals("EXIT")) {
                return;
            }
            if (command.equals("1")) {
                System.out.println("What would you like to call this schedule?");
                String newScheduleName = s.next();
                String newScheduleSemester = "Fall";
                while(true) {
                    System.out.println("What Semester is this schedule for");
                    newScheduleSemester = s.next();
                    if(newScheduleSemester.equalsIgnoreCase("SPRING") || newScheduleSemester.equalsIgnoreCase("FALL")){
                        break;
                    }else{
                        System.out.println("Invalid semester: must be Spring or Fall");
                        s.nextLine();
                    }
                }
                int year = 2020;
                while(true) {
                    try {
                        System.out.println("What year is this schedule for?");
                        year = s.nextInt();
                        break;
                    }catch(Exception e){
                        System.out.println("Invalid input");
                        s.nextLine();
                    }
                }
                current.addNewSchedule(schedCount, newScheduleSemester, year, newScheduleName);
                currentSchedule = current.getSchedules().getLast();
                System.out.println(currentSchedule.getScheduleName());
                schedCount++;
            }

            if (command.equals("2")) {
                System.out.println(currentSchedule.toStringEx());
            }
            if (command.equals("3")) {
                //do search method here
                searchCourses();}
            if (command.equals("4")) {
                navigateSchedules(s, current);
            }
        }

    }

    /**
     * allows the user to edit their profile
     * @param s
     * @param current
     */
    public static void editProfile(Scanner s, Student current) throws InterruptedException {
        String state = "0";
        int temp;

        while (!state.equals("6")) {
            if (state.equals("0")) {
                System.out.println("You are currently editing " + current.getUsername() + "'s profile" );
                System.out.println();
            }

            System.out.println("Enter 1 to edit your username");
            System.out.println("Enter 2 to add or remove a major");
            System.out.println("Enter 3 to add or remove a minor");
            System.out.println("Enter 4 to change your class standing");
            System.out.println("Enter 5 to enter in previously taken courses");
            System.out.println("Enter 6 to return to home");
            state = s.next();

            if (state.equals("1")) {
                System.out.println("Your current username is: " + current.getUsername());
                System.out.println("Or press B to go back");
                String newName = s.next();
                if(newName.equals("B")){
                    return;
                }
                current.setUsername(newName);
                System.out.println("Your username has been changed to " + current.getUsername());
                TimeUnit.SECONDS.sleep(5);
                return;

            } else if (state.equals("2")) {
                System.out.print("Your current major(s) are: ");
                for(int i = 0; i < current.getMajors().size(); i++){
                    System.out.print(current.getMajors().get(i) + " ");
                }
                //new line
                System.out.println();
                System.out.println("Enter 1 to add a major or enter 2 to remove a major or B to go back");
                String majorControl = s.next();
                if(majorControl.equals("B")){
                    return;
                }
                else if(majorControl.equals("1")){
                    System.out.println("Enter what major you would like to add");
                    //do major adding logic
                }
                else if(majorControl.equals("2")){
                    for (int i = 0; i < current.getMajors().size(); i++) {
                        System.out.print(i+1);
                        System.out.print(".)");
                        System.out.print(current.getMajors().get(i));
                        System.out.println();
                    }
                    System.out.println("Enter the number of the major you would like to remove");
                    String toRemove = s.next();

                    for(Character c : toRemove.toCharArray()){
                        if(!Character.isDigit(c)){
                            return;
                        }
                    }
                    if(Integer.parseInt(toRemove)-1 < current.getMajors().size() && Integer.parseInt(toRemove)-1 >= 0) {
                        current.getMajors().remove(Integer.parseInt(toRemove) - 1);
                    }

                }

            } else if (state.equals("3")) {
                System.out.print("Your current minor(s) are: ");
                for(int i = 0; i < current.getMinors().size(); i++){
                    System.out.print(current.getMinors().get(i) + " ");
                }
                //new line
                System.out.println();
                System.out.println("Enter 1 to add a minor or enter 2 to remove a minor or B to go back");
                String minorControl = s.next();
                if(minorControl.equals("B")){
                    return;
                }
                else if(minorControl.equals("1")){
                    System.out.println("Enter what minor you would like to add");
                    //do minor adding logic
                }
                else if(minorControl.equals("2")){
                    for (int i = 0; i < current.getMinors().size(); i++) {
                        System.out.print(i+1);
                        System.out.print(".)");
                        System.out.print(current.getMinors().get(i));
                        System.out.println();
                    }
                    System.out.println("Enter the number of the minor you would like to remove");
                    String toRemove = s.next();

                    for(Character c : toRemove.toCharArray()){
                        if(!Character.isDigit(c)){
                            return;
                        }
                    }
                    if(Integer.parseInt(toRemove)-1 < current.getMinors().size() && Integer.parseInt(toRemove)-1 >= 0) {
                        current.getMinors().remove(Integer.parseInt(toRemove) - 1);
                    }

                }

            }
            else if (state.equals("4")) {
                System.out.println("Your current class standing is " + current.getClassStanding());
                System.out.println("Enter 1 to change tour class standing or B to go back");
                String classControl = s.next();

                if(classControl.equals("B")){
                    return;
                }else if(classControl.equals("1")){
                    System.out.println("1.) Freshman");
                    System.out.println("2.) Sophmore");
                    System.out.println("3.) Junior");
                    System.out.println("4.) Senior");
                    System.out.println("5.) Other");
                    //do logic for enum change
                }
            }
            else if (state.equals("5")){
                //do logic for adding already taken courses
            }
        }
    }

    /**
     * handles allowing the user to navigate their schedules
     * @param s
     * @param current
     */
    public static void navigateSchedules(Scanner s, Student current) {
        String state = "0";
        int temp;

        while (!state.equals("6")) {
            if (state.equals("0")) {
                System.out.println("You are currently editing: " + currentSchedule.getScheduleName());
                System.out.println();
            }

            System.out.println("Enter 1 to add a course to " + currentSchedule.getScheduleName());
            System.out.println("Enter 2 to remove a course from " + currentSchedule.getScheduleName());
            System.out.println("Enter 3 to rename this schedule");
            System.out.println("Enter 4 to switch schedule");
            System.out.println("Enter 5 to save your current schedule");
            System.out.println("Enter 6 to return to home");
            state = s.next();

            if (state.equals("1")) {
                // go to search method
                // add searched class to schedule
                searchCourses();

            } else if (state.equals("2")) {

                // print out schedule in a list format
                for (int i = 0; i < currentSchedule.getCourses().size(); i++) {
                    System.out.println(i + 1 + ".)" + currentSchedule.getCourses().get(i).toString());
                }
                System.out.println("Enter the number of course you would like to remove from your schedule");
                System.out.println("Or, press B to go back");
                String toRemove = s.next();
                if(toRemove.equals("B")){
                    return;
                }
                for(Character c : toRemove.toCharArray()){
                    if(!Character.isDigit(c)){
                        return;
                    }
                }
                if(Integer.parseInt(toRemove)-1 < currentSchedule.getCourses().size() && Integer.parseInt(toRemove)-1 >= 0) {
                    currentSchedule.getCourses().remove(Integer.parseInt(toRemove) - 1);
                }
            } else if (state.equals("3")) {
                System.out.println("This schedules current name is " + currentSchedule.getScheduleName());
                System.out.println("Enter what you would like to name this schedule");
                String newName = s.next();
                File toDelete = new File(current.getStudentID() + "_" + current.getUsername(),
                        currentSchedule.getScheduleID() + "_" + currentSchedule.getScheduleName());

                if(toDelete.delete()){
                    System.out.println("Successfully renamed schedule!");
                }
                else{
                    System.out.println("Was unable to replace old schedule");
                }
                currentSchedule.setScheduleName(newName);
                currentSchedule.saveSchedule(current.getStudentID() + "_" + current.getUsername());
                current.setSchedules(current.loadAllSchedules(current.getStudentID() + "_" + current.getUsername()));

            } else if (state.equals("4")) {
                for (int i = 0; i < current.getSchedules().size(); i++) {
                    System.out.print(i + 1 + ".) ");
                    System.out.println(current.getSchedules().get(i).toStringEx());}

                System.out.println("\nEnter the number of the schedule you would like to edit");
                temp = s.nextInt() - 1;


                if (temp > current.getSchedules().size()) {
                    currentSchedule = current.getSchedules().getLast();
                } else if (temp < 1) {
                    currentSchedule = current.getSchedules().getFirst();
                } else {
                    currentSchedule = current.getSchedules().get(temp);
                }
            } else if (state.equals("5")){
                currentSchedule.saveSchedule(current.getStudentID() + "_" + current.getUsername());
                current.setSchedules(current.loadAllSchedules(current.getStudentID() + "_" + current.getUsername()));
            }
        }
    }

    /**
     * handles user IO for the search menu, navigation, and functionality
     */
    private static void searchCourses(){
        //User input setup
        Scanner input = new Scanner(System.in);
        String query = "";
        ArrayList<Course> results;
        ArrayList<Filter> filters = new ArrayList<>();
        Filter semesterFilter = new Filter(new ArrayList<String>(List.of(currentSchedule.getSemester(),
                ""+currentSchedule.getYear())), Filter.FilterType.SEMESTER);

        // Create search instance
        Search s = new Search("", courseCatalog, filters, semesterFilter);

        while(!query.equals("Q")){
            System.out.println("---Your Schedule---");
            for(Course c : currentSchedule.getCourses()){
                System.out.println(c);
            }
            System.out.println("-------------------");
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
                for(int i = 0; i < results.size(); i++){
                    System.out.println(String.format("%4d", i) + " - " + displayCourse(results.get(i)));
                }
                System.out.println("To add a course to your schedule, type the number to the left of the course code (type 'B' to go back):");
                try{
                    int classToAdd = input.nextInt();
                    if(classToAdd >= 0 && classToAdd < results.size()){
                        // Add course to current schedule
                        if(!currentSchedule.addCourse(results.get(classToAdd)) && !currentSchedule.getCourses().contains(results.get(classToAdd))) {
                            System.out.println("Could not add " + results.get(classToAdd) + " to your schedule due to time conflict or a duplicate course.");
                        }else{
                            System.out.println(results.get(classToAdd) + " was added to your schedule.");
                        }
                        // Clear the input
                        input.nextLine();
                    }
                }catch(Exception e){
                    // Clear the input and do nothing -> go back to search
                    input.nextLine();
                }
            }
        }
    }

    /**
     * Creates a string of the relevant info for a course in a nicely formatted way
     * @param c is the course to format
     * @return a string
     */
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

    /**
     * Removes a filter
     * @param s is the Search instance
     * @param filters is the list of applied filters
     */
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

    /**
     * Adds a filter
     * @param filters is the list of applied filters
     * @return the filter to be added
     */
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

    /**
     * Reads the 2020-2021.csv file and parses the data into an ArrayList of course objects
     * @throws FileNotFoundException if the file isn't found
     * @throws ParseException if something weird happens (it shouldn't, hopefully. . .)
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

    public ArrayList<Course> getCourseCatalog() {
        return courseCatalog;
    }

}