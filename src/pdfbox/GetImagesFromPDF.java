/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfbox;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

/**
 *
 * @author Pizaini
 */
public class GetImagesFromPDF {
    public static void main(String[] args) {
        try {
            String sourceDir = "D:/PdfBox/04-Request-Headers.pdf";// Paste pdf files in PDFCopy folder to read
            String destinationDir = "D:/PdfBox/";
            File oldFile = new File(sourceDir);
            if (oldFile.exists()) {
            PDDocument document = PDDocument.load(sourceDir);

            List<PDPage> list = document.getDocumentCatalog().getAllPages();

            String fileName = oldFile.getName().replace(".pdf", "_cover");
            int totalImages = 1;
            for (PDPage page : list) {
                PDResources pdResources = page.getResources();

                Map pageImages = pdResources.getImages();
                if (pageImages != null) {

                    Iterator imageIter = pageImages.keySet().iterator();
                    while (imageIter.hasNext()) {
                        String key = (String) imageIter.next();
                        PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                        pdxObjectImage.write2file(destinationDir + fileName+ "_" + totalImages);
                        totalImages++;
                    }
                }
            }
        } else {
            System.err.println("File not exists");
        }
    } catch (Exception e) {
    }
    }
}