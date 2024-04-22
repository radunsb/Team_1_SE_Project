package classes;

import java.io.IOException;


public class PDFBoxToText {
    public static void main(String[] args) {
        PDFManager pdfManager = new PDFManager();
        pdfManager.setFilePath("C:\\Users\\GROSSMANRC20\\IdeaProjects\\Team1_SE_project2\\CS.pdf");
        try{
            String firstPageText = pdfManager.toText();
            //System.out.println(firstPageText);
            System.out.println(firstPageText);
            String[] token = firstPageText.split(" ");

            System.out.println("\n\n");
            for (int i = 0; i < token.length; i++){
                if(token[i].equals("Freshman")){
                    for (int j = i; j < token.length - i; j++) {
                        System.out.println(token[j]);
                    }
                }
            }

//            for (int i = 0; i < token.length; i++){
//                if(token[i].contains("..")){
//                    System.out.println(token[i-3].strip() + " " + token[i-2].strip() + " " + token[i-1].strip() + " " + token[i+1].strip()+ " " + token[i+2].strip()+ " " + token[i+3].strip());
//                    //System.out.println(token[i]);
//                }
//                else{
//                }
//            }

        }catch(IOException e){
            System.out.println("Shit Broke");
        }
    }
}