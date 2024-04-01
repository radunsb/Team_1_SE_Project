package classes;

import java.util.ArrayList;
//Pull test request
public class Filter {
    private ArrayList<String> input;
    private FilterType type; //see FilterType enum below
    public enum FilterType{
        TIME,
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
