package classes;

import java.io.IOException;
import java.util.ArrayList;



public class PDFBoxToText {

    private static ArrayList<Course> recomendedCourses;


    public static void main(String[] args) {
        PDFManager pdfManager = new PDFManager();
        pdfManager.setFilePath("C:\\Users\\GROSSMANRC20\\IdeaProjects\\Team1_SE_project2\\CS.pdf");
        try{
            String firstPageText = pdfManager.toText();

            String[] token = firstPageText.split(" ");

            System.out.println("\n\n");
            for (int i = 0; i < token.length; i++){
                if(isAllUpper(token[i]) && !isNumeric(token[i])){
                    System.out.println(token[i] + token[i+1]);
                }
            }

        }catch(IOException e){
            System.out.println("Shit Broke");
        }
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