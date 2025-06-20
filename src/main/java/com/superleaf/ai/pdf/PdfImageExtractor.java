package com.superleaf.ai.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.cos.COSName;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class PdfImageExtractor {

    public static List<byte[]> extractImages(File pdfFile) throws IOException {
        List<byte[]> images = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            for (PDPage page : document.getPages()) {
                for (COSName name : page.getResources().getXObjectNames()) {
                    PDXObject xObject = page.getResources().getXObject(name);
                    if (xObject instanceof PDImageXObject) {
                        PDImageXObject image = (PDImageXObject) xObject;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image.getImage(), "png", baos);
                        images.add(baos.toByteArray());
                    }
                }
            }
        }

        return images;
    }
}
