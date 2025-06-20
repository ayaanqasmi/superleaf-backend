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
            File pdfFile = new File(
                    "C:\\Users\\ayaan\\projects\\current\\superleaf\\backend\\demo\\src\\main\\java\\com\\superleaf\\ai\\pdf\\sample_research_paper.pdf");
            if (!pdfFile.exists()) {
                System.err.println("PDF file not found: " + pdfFile.getAbsolutePath());
                System.exit(1);
            }

            // Extract text
            String text = PdfTextExtractor.extractText(pdfFile);
            // Save to .txt
            Path outputPath = Path.of("output/extracted-text.txt");
            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, text, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Text saved to " + outputPath.toAbsolutePath());

            // Extract images
            List<PdfImageExtractor.ImageWithCaption> imagesWithCaptions = PdfImageExtractor
                    .extractImagesWithCaptions(pdfFile);
            System.out.println("Extracted " + imagesWithCaptions.size() + " images with captions");

            // Save images and captions to disk
            int count = 0;
            for (PdfImageExtractor.ImageWithCaption item : imagesWithCaptions) {
                File imageFile = new File("output/image_" + count + ".png");
                File captionFile = new File("output/caption_" + count + ".txt");

                java.nio.file.Files.createDirectories(imageFile.getParentFile().toPath());

                java.nio.file.Files.write(imageFile.toPath(), item.imageBytes);
                java.nio.file.Files.writeString(captionFile.toPath(), item.caption != null ? item.caption : "");

                System.out.println("Saved image:   " + imageFile.getAbsolutePath());
                System.out.println("Saved caption: " + captionFile.getAbsolutePath());

                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
