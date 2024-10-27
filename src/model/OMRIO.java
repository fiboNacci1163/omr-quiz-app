package model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OMRIO {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the image path of the OMR sheet.");
            return;
        }

        String imgPath = args[0];

        List<Integer> correctAnswers = Arrays.asList(1, 2, 4, 3, 1);

        int[] markedAnswers = readOMRSheet(imgPath, correctAnswers.size());

        int correctCount = 0;
        int incorrectCount = 0;

        System.out.println("OMR Detection Results:");
        for (int i = 0; i < markedAnswers.length; i++) {
            char markedAnswerChar = (char) ('A' + markedAnswers[i] - 1);
            char correctAnswerChar = (char) ('A' + correctAnswers.get(i) - 1);
            boolean isCorrect = markedAnswers[i] == correctAnswers.get(i);

            System.out.println("Q" + (i + 1) + ": Marked = " + markedAnswerChar + ", Correct = " + correctAnswerChar);

            if (isCorrect) {
                correctCount++;
            } else {
                incorrectCount++;
            }
        }

        System.out.println("Total Correct Answers: " + correctCount);
        System.out.println("Total Incorrect Answers: " + incorrectCount);
    }

    public static int[] readOMRSheet(String imgPath, int numQuestions) {
        if (imgPath.isEmpty())
            imgPath = "omr_sheet_filled.png";

        BufferedImage omrSheet;
        try {
            omrSheet = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the OMR sheet image. Ensure the file path is correct.", e);
        }

        int bubbleSize = 30;
        int bubbleSpacing = 60;
        int questionSpacing = 80;
        int startingY = 100;

        List<Integer> answers = new ArrayList<>();

        for (int i = 0; i < numQuestions; i++) {
            boolean foundAnswer = false;
            for (int j = 0; j < 4; j++) {
                int x = 100 + j * bubbleSpacing;
                int y = startingY + i * questionSpacing - 20;

                BufferedImage bubbleArea = omrSheet.getSubimage(x, y, bubbleSize, bubbleSize);

                if (isFilled(bubbleArea)) {
                    answers.add(j + 1);
                    foundAnswer = true;
                    break;
                }
            }

            if (!foundAnswer) {
                answers.add(0);
            }
        }

        return answers.stream().mapToInt(i -> i).toArray();
    }

    private static boolean isFilled(BufferedImage bubbleArea) {
        int blackThreshold = 128;
        int filledThreshold = (int) (0.7 * bubbleArea.getWidth() * bubbleArea.getHeight());

        int blackPixelCount = 0;

        for (int y = 0; y < bubbleArea.getHeight(); y++) {
            for (int x = 0; x < bubbleArea.getWidth(); x++) {
                int rgb = bubbleArea.getRGB(x, y);
                Color color = new Color(rgb);
                int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                if (grayValue < blackThreshold) {
                    blackPixelCount++;
                }
            }
        }

        return blackPixelCount >= filledThreshold;
    }

    public static BufferedImage createOMR(String omrLabel, int[] answers) {

        int width = 380;
        int height = 100 + 80 * answers.length;

        BufferedImage omrSheet = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = omrSheet.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 18);
        g2d.setFont(font);

        g2d.drawString(omrLabel, 50, 50);

        int bubbleSize = 30;
        int bubbleSpacing = 60;
        int questionSpacing = 80;
        int startingY = 100;

        for (int i = 1; i <= answers.length; i++) {
            g2d.drawString("Q" + i, 50, startingY + (i - 1) * questionSpacing);

            for (int j = 0; j < 4; j++) {
                int x = 100 + j * bubbleSpacing;
                int y = startingY + (i - 1) * questionSpacing - 20;

                if (answers[i - 1] == j + 1) {
                    g2d.fillOval(x, y, bubbleSize, bubbleSize);
                } else {
                    g2d.drawOval(x, y, bubbleSize, bubbleSize);
                }
                g2d.drawString(Character.toString((char) ('A' + j)), x + 10, y + 20);
            }
        }

        g2d.dispose();

        return omrSheet;
    }

    public static void saveImage(BufferedImage omrSheet, String filename) {
        if (filename.isEmpty()) filename = "omr_sheet_filled.png";
        else filename += ".png";

        filename = "result/" + filename;

        try {
            File outputfile = new File(filename);
            ImageIO.write(omrSheet, "png", outputfile);
            System.out.println("OMR sheet image generated: " + filename);
            JOptionPane.showMessageDialog(null, "OMR image saved to " + outputfile.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
