package classes;

import java.util.ArrayList;
//Pull test request
public class Filter {
    private ArrayList<String> input;
    private FilterType type; //see FilterType enum below
    public enum FilterType{
        //Format: [startTime, endTime]
        //example: ["9:00 AM", "9:50 AM"]
        TIME,
        //Format: [day1, day2... etc]
        //example: ["M", "W", "F"]
        DAY,
        SEMESTER
    }

    public Filter(ArrayList<String> input, FilterType type) {
        this.input = input;
        this.type = type;
    }

    public ArrayList<String> getInput() {
        return input;
    }

    public void setInput(ArrayList<String> input) {
        this.input = input;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

}
