package ui;

import model.OMRIO;
import model.Quiz;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static ui.SwingHelper.*;

public class ScanOMRWindow {
    private static JTextField studentIdField;
    private static JLabel resultLabel;
    private static int correctCount;
    private static int incorrectCount;
    private static String imgPath;
    private static JFrame frame;
    private static JLabel correctLabel;
    private static JLabel incorrectLabel;
    private static JButton scanButton;

    public static void show ()
    {
        correctCount = 0;
        incorrectCount = 0;

        frame = new JFrame("Scan OMR");

                JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Student ID: "));
        studentIdField = new JTextField(15);
        inputPanel.add(studentIdField);

        JButton loadButton = new JButton("Load Image");
        inputPanel.add(loadButton);

        scanButton = new JButton("Scan");
        scanButton.setEnabled(false);         inputPanel.add(scanButton);

        frame.add(inputPanel, BorderLayout.NORTH);


        resultLabel = new JLabel();
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(resultLabel, BorderLayout.CENTER);

        JPanel countPanel = new JPanel();
        correctLabel = new JLabel("Correct Answers: " + correctCount);
        incorrectLabel = new JLabel("Incorrect Answers: " + incorrectCount);
        countPanel.add(correctLabel);
        countPanel.add(incorrectLabel);
        frame.add(countPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                setFile(selectedFile);
            }
        });


        scanButton.addActionListener(e -> {
            if (studentIdField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter Student ID before scanning.");
                return;
            }
            analyzeImage();             JOptionPane.showMessageDialog(null, "Your score: " + correctCount + "/" + (correctCount+incorrectCount), "Quiz Result", JOptionPane.INFORMATION_MESSAGE);

        });

        packScaleWindow(frame, 1.25, 1);
        centerShowWindow(frame);

    }


    private static void setFile(File selectedFile) {
        imgPath = selectedFile.getAbsolutePath();
        processImage(imgPath);
        scanButton.setEnabled(true); 
        try {
            String s = selectedFile.getName();
            String[] ss = s.substring(0, s.length() - 4).split(" - ");
            String studentID = ss[1];
            studentIdField.setText(studentID);
        }
        catch (Exception ex) {
                    }
    }


    private static void processImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            resultLabel.setIcon(new ImageIcon(image));
            packScaleWindow(frame, 1.25, 1);
            frame.revalidate();
            frame.repaint();
            centerShowWindow(frame);
                    } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading image: " + imgPath + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void analyzeImage() {
                        Quiz quiz = new Quiz();
        quiz.loadQuiz();
        int numQuestions = quiz.getQuestions().size();
        int[] selectedChoices = OMRIO.readOMRSheet(imgPath, numQuestions);
        correctCount = quiz.gradeQuiz(selectedChoices);
        incorrectCount = numQuestions - correctCount;

        correctLabel.setText("Correct Answers: " + correctCount);
        incorrectLabel.setText("Incorrect Answers: " + incorrectCount);
    }

    public static void showResult(String filename) {
        show();
        File selecetedFile = new File(filename);
        setFile(selecetedFile);
        analyzeImage();
    }
}

