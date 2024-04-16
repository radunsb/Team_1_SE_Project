package classes;

import java.io.IOException;


public class PDFBoxToText {
    public static void main(String[] args) {
        PDFManager pdfManager = new PDFManager();
        pdfManager.setFilePath("C:\\Users\\GROSSMANRC20\\IdeaProjects\\Team1_SE_project2\\CS.pdf");
        try{
            String firstPageText = pdfManager.toText();
            System.out.println(firstPageText);

//            System.out.println("\n\n\n\n");
//            String secondPageText = pdfManager.toText(1,1);
//            System.out.println(secondPageText);


        }catch(IOException e){
            System.out.println("Shit Broke");
        }
    }
}