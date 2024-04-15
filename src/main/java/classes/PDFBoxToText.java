package classes;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;


public class PDFBoxToText {
    public static void main(String[] args) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage firstpage = new PDPage();
        doc.addPage(firstpage);
        doc.save("mypdf.pdf");
        System.out.println("PDF created");

        doc.close();

    }
}
