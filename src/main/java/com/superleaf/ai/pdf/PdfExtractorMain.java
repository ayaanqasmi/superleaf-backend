package com.superleaf.ai.pdf;

import java.io.File;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
public class PdfExtractorMain {

    public static void main(String[] args) {
        try {
            File pdfFile = new File("C:\\Users\\ayaan\\projects\\current\\superleaf\\backend\\demo\\src\\main\\java\\com\\superleaf\\ai\\pdf\\sample_research_paper.pdf");
            if (!pdfFile.exists()) {
                System.err.println("PDF file not found: " + pdfFile.getAbsolutePath());
                System.exit(1);
            }

            // Extract text
            String text = PdfTextExtractor.extractText(pdfFile);
            //Save to .txt
            Path outputPath = Path.of("output/extracted-text.txt");
            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, text, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            System.out.println("Text saved to " + outputPath.toAbsolutePath());

            // Extract images
            List<byte[]> images = PdfImageExtractor.extractImages(pdfFile);
            System.out.println("Extracted " + images.size() + " images");
            
            // save images to disk to verify
            int count = 0;
            for (byte[] imgBytes : images) {
                File output = new File("output/image_" + (count++) + ".png");
                java.nio.file.Files.createDirectories(output.getParentFile().toPath());
                java.nio.file.Files.write(output.toPath(), imgBytes);
                System.out.println("Saved image to " + output.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
