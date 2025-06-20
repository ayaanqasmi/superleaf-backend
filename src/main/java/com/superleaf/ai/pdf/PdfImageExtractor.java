package com.superleaf.ai.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfImageExtractor {

    public static class ImageWithCaption {
        public byte[] imageBytes;
        public String caption;

        public ImageWithCaption(byte[] imageBytes, String caption) {
            this.imageBytes = imageBytes;
            this.caption = caption;
        }
    }

    public static List<ImageWithCaption> extractImagesWithCaptions(File pdfFile) throws IOException {
    List<ImageWithCaption> result = new ArrayList<>();

    try (PDDocument document = PDDocument.load(pdfFile)) {
        for (PDPage page : document.getPages()) {
            PDRectangle mediaBox = page.getMediaBox();
            float pageWidth = mediaBox.getWidth();
            float pageHeight = mediaBox.getHeight();

            ImagePositionExtractor extractor = new ImagePositionExtractor(page);
            extractor.processPage(page);

            for (ImagePositionExtractor.PositionedImage img : extractor.getImages()) {
                // Save image bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img.image.getImage(), "png", baos);

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                float marginHeight = 60f;
                float marginAboveY = Math.min(img.y + img.height + marginHeight, pageHeight);
                float marginBelowY = Math.max(img.y - marginHeight, 0);

                float regionX = 0;
                float regionWidth = pageWidth;

                if (img.width < pageWidth / 2) {
                    // Restrict region to left or right half based on x position
                    if (img.x < pageWidth / 2) {
                        regionX = 0;
                        regionWidth = pageWidth / 2;
                    } else {
                        regionX = pageWidth / 2;
                        regionWidth = pageWidth / 2;
                    }
                }

                // Define text region below the image
                Rectangle belowRegion = new Rectangle(
                    (int) regionX,
                    (int) marginBelowY,
                    (int) regionWidth,
                    (int) Math.min(marginHeight, img.y) // Ensure we don't overflow page bottom
                );

                // Define text region above the image
                Rectangle aboveRegion = new Rectangle(
                    (int) regionX,
                    (int) img.y + (int) img.height,
                    (int) regionWidth,
                    (int) Math.min(marginHeight, pageHeight - (img.y + img.height))
                );

                stripper.addRegion("below", belowRegion);
                stripper.addRegion("above", aboveRegion);
                stripper.extractRegions(page);

                String captionAbove = stripper.getTextForRegion("above").trim();
                String captionBelow = stripper.getTextForRegion("below").trim();
                String combinedCaption = (captionAbove + "\n" + captionBelow).trim();

                result.add(new ImageWithCaption(baos.toByteArray(), combinedCaption));
            }
        }
    }

    return result;
}

    private static class ImagePositionExtractor extends PDFGraphicsStreamEngine {

        private final List<PositionedImage> images = new ArrayList<>();

        protected ImagePositionExtractor(PDPage page) {
            super(page);
        }

        public List<PositionedImage> getImages() {
            return images;
        }

        @Override
        public void drawImage(PDImage pdImage) throws IOException {
            if (pdImage instanceof PDImageXObject) {
                PDImageXObject image = (PDImageXObject) pdImage;

                Matrix ctm = getGraphicsState().getCurrentTransformationMatrix();
                float x = ctm.getTranslateX();
                float y = ctm.getTranslateY();
                float width = ctm.getScalingFactorX();
                float height = ctm.getScalingFactorY();

                images.add(new PositionedImage(image, x, y, width, height));
            }
        }

        static class PositionedImage {
            public PDImageXObject image;
            public float x, y, width, height;

            public PositionedImage(PDImageXObject image, float x, float y, float width, float height) {
                this.image = image;
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
            }
        }

        // Unused overrides (no-ops for this extractor)
        @Override
        public void strokePath() {
        }

        @Override
        public void fillPath(int windingRule) {
        }

        @Override
        public void clip(int windingRule) {
        }

        @Override
        public void moveTo(float x, float y) {
        }

        @Override
        public void lineTo(float x, float y) {
        }

        @Override
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        }

        @Override
        public Point2D getCurrentPoint() {
            return null;
        }

        @Override
        public void closePath() {
        }

        @Override
        public void endPath() {
        }

        @Override
        public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {

        }

        @Override
        public void fillAndStrokePath(int windingRule) throws IOException {

        }

        @Override
        public void shadingFill(COSName shadingName) throws IOException {

        }
    }
}
